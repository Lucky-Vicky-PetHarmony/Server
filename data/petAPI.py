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
        parse.quote_plus('bgnde'): '20240101',  # 검색 시작일
        parse.quote_plus('endde'): '20240817',  # 검색 종료일
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
        desertion_no = item.find('desertionNo').text if item.find('desertionNo') is not None else None
        process_state = item.find('processState').text if item.find('processState') is not None else None

        if process_state and "종료" in process_state:
            # process_state에 "종료"가 포함된 경우 데이터 삭제
            cursor.execute("DELETE FROM pet_info WHERE desertion_no = %s", (desertion_no,))
            rowcount += 1
        else:
            # 데이터베이스에서 해당 데이터가 존재하는지 확인
            cursor.execute("SELECT process_state FROM pet_info WHERE desertion_no = %s", (desertion_no,))
            result = cursor.fetchone()

            if result:
                # 기존 데이터가 존재하는 경우 process_state가 변경되었는지 확인
                if result[0] != process_state:
                    sql = """
                    UPDATE pet_info SET
                        happen_dt = %s,
                        happen_place = %s,
                        kind_cd = %s,
                        color_cd = %s,
                        age = %s,
                        weight = %s,
                        notice_no = %s,
                        notice_sdt = %s,
                        notice_edt = %s,
                        popfile = %s,
                        process_state = %s,
                        sex_cd = %s,
                        neuter_yn = %s,
                        special_mark = %s,
                        care_nm = %s,
                        charge_nm = %s,
                        officetel = %s,
                        notice_comment = %s
                    WHERE desertion_no = %s
                    """

                    values = (
                        datetime.strptime(item.find('happenDt').text, '%Y%m%d') if item.find('happenDt') is not None else None,
                        item.find('happenPlace').text if item.find('happenPlace') is not None else None,
                        item.find('kindCd').text if item.find('kindCd') is not None else None,
                        item.find('colorCd').text if item.find('colorCd') is not None else None,
                        item.find('age').text if item.find('age') is not None else None,
                        item.find('weight').text if item.find('weight') is not None else None,
                        item.find('noticeNo').text if item.find('noticeNo') is not None else None,
                        datetime.strptime(item.find('noticeSdt').text, '%Y%m%d') if item.find('noticeSdt') is not None else None,
                        datetime.strptime(item.find('noticeEdt').text, '%Y%m%d') if item.find('noticeEdt') is not None else None,
                        item.find('popfile').text if item.find('popfile') is not None else None,
                        process_state,
                        item.find('sexCd').text if item.find('sexCd') is not None else None,
                        item.find('neuterYn').text if item.find('neuterYn') is not None else None,
                        item.find('specialMark').text if item.find('specialMark') is not None else None,
                        item.find('careNm').text if item.find('careNm') is not None else None,
                        item.find('chargeNm').text if item.find('chargeNm') is not None else None,
                        item.find('officetel').text if item.find('officetel') is not None else None,
                        item.find('noticeComment').text if item.find('noticeComment') is not None else None,
                        desertion_no
                    )

                    cursor.execute(sql, values)
                    rowcount += 1

            else:
                # 새 데이터인 경우 삽입
                sql = """
                INSERT INTO pet_info (
                    desertion_no, happen_dt, happen_place, kind_cd, color_cd, age, weight, notice_no, notice_sdt, notice_edt, 
                    popfile, process_state, sex_cd, neuter_yn, special_mark, care_nm, charge_nm, officetel, notice_comment
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """

                values = (
                    desertion_no,
                    datetime.strptime(item.find('happenDt').text, '%Y%m%d') if item.find('happenDt') is not None else None,
                    item.find('happenPlace').text if item.find('happenPlace') is not None else None,
                    item.find('kindCd').text if item.find('kindCd') is not None else None,
                    item.find('colorCd').text if item.find('colorCd') is not None else None,
                    item.find('age').text if item.find('age') is not None else None,
                    item.find('weight').text if item.find('weight') is not None else None,
                    item.find('noticeNo').text if item.find('noticeNo') is not None else None,
                    datetime.strptime(item.find('noticeSdt').text, '%Y%m%d') if item.find('noticeSdt') is not None else None,
                    datetime.strptime(item.find('noticeEdt').text, '%Y%m%d') if item.find('noticeEdt') is not None else None,
                    item.find('popfile').text if item.find('popfile') is not None else None,
                    process_state,
                    item.find('sexCd').text if item.find('sexCd') is not None else None,
                    item.find('neuterYn').text if item.find('neuterYn') is not None else None,
                    item.find('specialMark').text if item.find('specialMark') is not None else None,
                    item.find('careNm').text if item.find('careNm') is not None else None,
                    item.find('chargeNm').text if item.find('chargeNm') is not None else None,
                    item.find('officetel').text if item.find('officetel') is not None else None,
                    item.find('noticeComment').text if item.find('noticeComment') is not None else None
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

print(f"{rowcount} 개의 기록이 삽입/업데이트/삭제되었습니다.")
