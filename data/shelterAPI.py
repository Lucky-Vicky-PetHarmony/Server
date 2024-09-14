import requests
from urllib import parse
import xml.etree.ElementTree as ET
import mysql.connector
from datetime import datetime
import time

# API URL 및 키 설정
url = 'http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic'
key = 'igjk1S52YiKgplP2y66Z%2F4KqaZwVaNRm3ebIvFIV65JS4hI22neHjrrErvTJxE%2FxY%2B%2Fc1IH%2B8SCGHOb0S2U8ww%3D%3D'

# MySQL 데이터베이스 연결
db = mysql.connector.connect(
    host="lkvk-database.c1ukcuwq4uyu.ap-northeast-2.rds.amazonaws.com",  # MySQL 서버 호스트
    user="root",  # MySQL 사용자명
    password="Petharmony123",
    database="petharmony",  # 사용할 데이터베이스 명
    port=3306  # MySQL 서버 포트
)
cursor = db.cursor()

# 모든 페이지의 데이터를 가져오기 위해 반복문 사용
page_no = 1
total_count = 0
rowcount = 0

while True:
    # 쿼리 파라미터 설정
    queryParams = '?' + parse.quote_plus("serviceKey") + '=' + key + '&' + parse.urlencode({
        parse.quote_plus('bgnde'): '20240909',  # 검색 시작일
        parse.quote_plus('endde'): '20240914',  # 검색 종료일
        parse.quote_plus('pageNo'): str(page_no),
        parse.quote_plus('numOfRows'): '1000',  # 한 번에 가져올 데이터 수
        parse.quote_plus('_type'): 'xml'
    })

    # 요청 URL 생성
    request_url = url + queryParams

    # API 요청
    response = requests.get(request_url)

    # 응답 상태 코드 확인
    print(f"응답 상태 코드: {response.status_code}")

    if response.status_code != 200:
        print("API 요청 실패, 상태 코드:", response.status_code)
        print("응답 내용:", response.text)
        break

    # XML 파싱
    try:
        root = ET.fromstring(response.content)
    except ET.ParseError as e:
        print("XML Parse Error:", e)
        print("응답 내용:", response.text)
        break

    # 응답 결과 코드 확인
    result_code = root.find('.//resultCode')
    if result_code is None or result_code.text != '00':
        result_msg = root.find('.//resultMsg').text if root.find('.//resultMsg') is not None else 'Unknown error'
        print(f"오류 {result_code.text}: {result_msg}")
        break

    # 총 데이터 개수 확인 (첫 페이지에서만 수행)
    if page_no == 1:
        total_count = int(root.find('.//totalCount').text)
        print(f"총 데이터 개수: {total_count}")

    # 데이터 처리
    items = root.findall('.//item')
    if not items:
        print("더 이상 데이터가 없습니다.")
        break

    for item in items:
        care_tel = item.find('careTel').text if item.find('careTel') is not None else None
        care_nm = item.find('careNm').text if item.find('careNm') is not None else None

        # care_tel과 care_nm 중복 확인
        cursor.execute("SELECT COUNT(*) FROM shelter_info WHERE care_tel = %s AND care_nm = %s", (care_tel, care_nm))
        if cursor.fetchone()[0] > 0:
            print(f"중복된 데이터: {care_tel}, {care_nm}")
            continue

        sql = """
        INSERT INTO shelter_info (
            care_nm, org_nm, save_trgt_animal, care_addr, lat, lng, 
            week_opr_stime, week_opr_etime, weekend_opr_stime, weekend_opr_etime,
            close_day, care_tel
        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """

        values = (
            care_nm,
            item.find('orgNm').text if item.find('orgNm') is not None else None,
            item.find('saveTrgtAnimal').text if item.find('saveTrgtAnimal') is not None else None,
            item.find('careAddr').text if item.find('careAddr') is not None else None,
            item.find('lat').text if item.find('lat') is not None else None,
            item.find('lng').text if item.find('lng') is not None else None,
            item.find('weekOprStime').text if item.find('weekOprStime') is not None else None,
            item.find('weekOprEtime').text if item.find('weekOprEtime') is not None else None,
            item.find('weekendOprStime').text if item.find('weekendOprStime') is not None else None,
            item.find('weekendOprEtime').text if item.find('weekendOprEtime') is not None else None,
            item.find('closeDay').text if item.find('closeDay') is not None else None,
            care_tel
        )

        cursor.execute(sql, values)
        rowcount += 1

    # 페이지 번호 증가
    page_no += 1

    # API 요청 간 딜레이 (0.5초)
    time.sleep(0.5)

    # 총 데이터 개수만큼 가져왔으면 종료
    if rowcount >= total_count:
        break

# 변경 사항 커밋 및 연결 종료
db.commit()
cursor.close()
db.close()

print(f"{rowcount} 개의 기록이 삽입/업데이트되었습니다.")
