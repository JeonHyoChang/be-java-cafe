INSERT INTO CAFE_USER(USERID, PASSWORD, NAME, EMAIL)
VALUES ('test', '1234', '텟슷텃', 'test01@naver.com');

INSERT INTO CAFE_USER(USERID, PASSWORD, NAME, EMAIL)
VALUES ('jhc1426', '1111', 'ManJu', '11111@gmail.com');

INSERT INTO CAFE_ARTICLE(WRITER, TITLE, CONTENTS, CAFEUSERID)
VALUES ('ManJu', '테스트01', '테스트용 글 입니돵', 2);

INSERT INTO CAFE_ARTICLE(WRITER, TITLE, CONTENTS, CAFEUSERID)
VALUES ('텟슷텃', '테스트02', 'TEST가 작성한 TEST', 1);

INSERT INTO CAFE_REPLY(WRITER, CONTENTS, ARTICLEID)
VALUES ('텟슷텃', '테스트용 댓글 일지도?!?!?!?', 1);