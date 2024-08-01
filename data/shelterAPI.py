import requests
from urllib import parse
import xml.etree.ElementTree as ET
import mysql.connector
from datetime import datetime

# API URL 및 키 설정
url = 'http://apis.data.go.kr/1543061/animalShelterSrvc/shelterInfo'
key = 'igjk1S52YiKgplP2y66Z%2F4KqaZwVaNRm3ebIvFIV65JS4hI22neHjrrErvTJxE%2FxY%2B%2Fc1IH%2B8SCGHOb0S2U8ww%3D%3D'

# MySQL 데이터베이스 연결
db = mysql.connector.connect(
    host="127.0.0.1",  # MySQL 서버 호스트
    user="pet",  # MySQL 사용자명
    password="Petharmony123@",
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
        careTel = item.find('careTel').text if item.find('careTel') is not None else None

        # 중복 확인
        cursor.execute("SELECT COUNT(*) FROM shelter_info WHERE careTel = %s", (careTel,))
        if cursor.fetchone()[0] > 0:
            print(f"중복된 데이터: {careTel}")
            continue

        sql = """
        INSERT INTO shelter_info (
            careNm, orgNm, divisionNm, saveTrgtAnimal, careAddr, jibunAddr, 
            lat, lng, dsignationDate, weekOprStime, weekOprEtime, weekCellStime, 
            weekCellEtime, weekendOprStime, weekendOprEtime, weekendCellStime, 
            weekendCellEtime, closeDay, vetPersonCnt, specsPersonCnt, medicalCnt, 
            breedCnt, quarabtineCnt, feedCnt, transCarCnt, careTel, dataStdDt
        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """

        values = (
            item.find('careNm').text if item.find('careNm') is not None else None,
            item.find('orgNm').text if item.find('orgNm') is not None else None,
            item.find('divisionNm').text if item.find('divisionNm') is not None else None,
            item.find('saveTrgtAnimal').text if item.find('saveTrgtAnimal') is not None else None,
            item.find('careAddr').text if item.find('careAddr') is not None else None,
            item.find('jibunAddr').text if item.find('jibunAddr') is not None else None,
            item.find('lat').text if item.find('lat') is not None else None,
            item.find('lng').text if item.find('lng') is not None else None,
            item.find('dsignationDate').text if item.find('dsignationDate') is not None else None,
            item.find('weekOprStime').text if item.find('weekOprStime') is not None else None,
            item.find('weekOprEtime').text if item.find('weekOprEtime') is not None else None,
            item.find('weekCellStime').text if item.find('weekCellStime') is not None else None,
            item.find('weekCellEtime').text if item.find('weekCellEtime') is not None else None,
            item.find('weekendOprStime').text if item.find('weekendOprStime') is not None else None,
            item.find('weekendOprEtime').text if item.find('weekendOprEtime') is not None else None,
            item.find('weekendCellStime').text if item.find('weekendCellStime') is not None else None,
            item.find('weekendCellEtime').text if item.find('weekendCellEtime') is not None else None,
            item.find('closeDay').text if item.find('closeDay') is not None else None,
            item.find('vetPersonCnt').text if item.find('vetPersonCnt') is not None else None,
            item.find('specsPersonCnt').text if item.find('specsPersonCnt') is not None else None,
            item.find('medicalCnt').text if item.find('medicalCnt') is not None else None,
            item.find('breedCnt').text if item.find('breedCnt') is not None else None,
            item.find('quarabtineCnt').text if item.find('quarabtineCnt') is not None else None,
            item.find('feedCnt').text if item.find('feedCnt') is not None else None,
            item.find('transCarCnt').text if item.find('transCarCnt') is not None else None,
            careTel,
            datetime.strptime(item.find('dataStdDt').text, '%Y-%m-%d') if item.find('dataStdDt') is not None else None
        )

        cursor.execute(sql, values)
        rowcount += 1

    # 페이지 번호 증가
    page_no += 1

    # 총 데이터 개수만큼 가져왔으면 종료
    if rowcount >= total_count:
        break

# 변경 사항 커밋 및 연결 종료
db.commit()
cursor.close()
db.close()

print(f"{rowcount} 개의 기록이 삽입되었습니다.")
