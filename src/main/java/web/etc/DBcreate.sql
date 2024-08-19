# 데이터베이스 생성 & 사용
drop database if exists fashionmanager;
create database fashionmanager;
use fashionmanager;

# 테이블 생성
drop table if exists productcategory;
create table productcategory(			# 상품카테고리
	prodcatecode int auto_increment,		# 상품카테고리코드PK
    prodcatename varchar(20),			# 상품카테고리명
    primary key (prodcatecode)
	);
    
drop table if exists color;
create table color(						# 색상
	colorcode int auto_increment,			# 색상코드PK
    colorname varchar(20),					# 색상명
    primary key (colorcode)
	);

drop table if exists product;
create table product(					# 상품
	prodcode int auto_increment,			# 상품코드PK
    prodname varchar(30) not null,			# 상품명
    prodprice mediumint not null,			# 상품가격
    prodgender char not null,				# 상품성별
	proddesc text,							# 상품설명
	primary key(prodcode)
	);

drop table if exists productdetail;
create table productdetail(				# 상품상세정보
	proddetailcode int auto_increment,		# 상품상세코드PK
    prodcode int not null,					# 상품코드FK
    prodcatecode int not null,				# 상품카테고리코드FK
    colorcode int not null,					# 색상코드FK
    prodsize varchar(10) not null,			# 상품사이즈
    prodfilename text,						# 상품이미지파일명
    proddate date not null default (current_date),	# 상품등록일
    primary key (proddetailcode),
    foreign key (prodcode) references product(prodcode) on delete cascade,
    foreign key (prodcatecode) references productcategory(prodcatecode),
    foreign key (colorcode) references color(colorcode)
    );
select * from productdetail a inner join product b on a.prodcode=b.prodcode inner join productcategory c on a.prodcatecode=c.prodcatecode inner join color d on a.colorcode=d.colorcode;
    
drop table if exists members;
create table members(					# 회원
	memcode int auto_increment,				# 회원코드PK
    memname	varchar(20) not null unique,	# 회원명
    memcontact varchar(13) not null,		# 회원연락처
    mememail varchar(50) not null unique,	# 회원이메일
    memgender char not null,				# 회원성별
    memcolor int,					# 회원 선호색상 (선택)FK
    memsize	varchar(10),					# 회원 선호사이즈 (선택)
    memjoindate	date not null default (current_date),	# 회원 등록날짜
    memlastdate date not null default (current_date),	# 회원 마지막 접속 날짜
    blacklist boolean default false,		# 블랙리스트 여부
    primary key (memcode),
    foreign key (memcolor) references color(colorcode)
    );

drop table if exists coupon;
create table coupon(					# 쿠폰
	coupcode int auto_increment,			# 쿠폰코드PK
    coupname varchar(20) not null,			# 쿠폰명
    coupsalerate tinyint not null, 			# 할인율 퍼센티지, 20 = 20% = 가격*0.8
    memcode int,							# 회원코드FK
    coupexpdate date not null,				# 쿠폰만료일
    primary key (coupcode),
	foreign key (memcode) references members(memcode) on delete set null
	);
    
drop table if exists orders;
create table orders(					# 주문
	ordcode int auto_increment,				# 주문코드PK
    memcode int,							# 회원코드FK
    orddate date not null default (current_date),	# 주문날짜
    primary key (ordcode),
    foreign key (memcode) references members(memcode) on delete set null
    );

drop table if exists orderdetail;
create table orderdetail(				# 주문상세내역
	orddetailcode int auto_increment,		# 주문상세코드PK
    ordcode	int not null,					# 주문코드FK
    proddetailcode int not null,			# 상품상세코드FK
    ordamount smallint not null,			# 주문수량
    ordstate tinyint,						# 주문현황 1. 주문완료 2. 배송시작 3. 반품 4. 취소 5. 정산완료
    coupcode int,							# 쿠폰코드FK
    ordprice mediumint not null,			# 주문시 가격
    primary key (orddetailcode),
    foreign key (ordcode) references orders(ordcode) on delete cascade,
    foreign key (proddetailcode) references productdetail(proddetailcode) on delete cascade,
    foreign key (coupcode) references coupon(coupcode)
    );

drop table if exists invlog;
create table invlog(					# 재고현황
	invlogcode int auto_increment,			# 재고현황코드PK
    proddetailcode int not null,			# 상품상세코드FK
    invlogchange smallint not null,			# 재고변경 (-2, 10 등)
    invlogdetail tinyint not null, 		# 재고변경내역 1. 재고입고 2. 판매 3. 취소 4. 환불
    invdate date not null default (current_date), # 재고 들어온 시간
    primary key (invlogcode),
    foreign key (proddetailcode) references productdetail(proddetailcode) on delete cascade
	);
drop table if exists support;
create table support(					# 상담
	supcode int auto_increment,				# 상담코드PK
    memcode	int,							# 회원코드FK
    supcategory tinyint not null,			# 상담카테고리 1. 반품문의 2. 상품문의 3. 배송문의 4. 회원문의 5. 기타
    suptitle text,							# 상담제목
    supcontent text,						# 상담내역
    supdate	date not null default (current_date),	# 상담날짜
    proddetailcode int,						# 상품상세코드FK
    supstate tinyint not null default 1, # 상담상태 1. 상담전 2. 진행중 3. 상담완료
    ordcode int,							# 주문코드FK
    primary key (supcode),
    foreign key (memcode) references members(memcode) on delete set null,
    foreign key (proddetailcode) references productdetail(proddetailcode) on delete set null,
    foreign key (ordcode) references orders(ordcode) on delete set null
	);

drop table if exists reply;
create table reply(						# 답장
	replycode int auto_increment,				# 답장코드PK
    supcode	int,								# 상담코드FK
    replycontent text,							# 상담내역
    replydate date not null default (current_date),	# 상담날짜
    primary key (replycode),
    foreign key (supcode) references support(supcode) on delete cascade
	);
    
drop table if exists admin;
create table admin(						# 관리자계정
	admincode int auto_increment,					# 관리자계정코드PK
    adminid	varchar(30),							# 관리자아이디
    adminpw varchar(30),							# 관리자비밀번호
    primary key (admincode)
	);
# 거래처테이블
drop table if exists vendor;
create table vendor(
	vendorcode int auto_increment , 				# 거래처코드PK
    vname varchar(30) not null, 					# 거래처이름
    vcontact varchar(13) not null unique , 			# 거래처연락처
    vaddress text , 								# 거래처주소
    vdate date default (current_date) , 			# 거래처등록일
    primary key(vendorcode)
    );

drop table if exists wholesaleproduct;
create table wholesaleproduct(
	wpcode int auto_increment , 					# 도매상품코드PK
	wpname varchar(30) not null , 					# 도매상품이름
    wpcost int not null , 							# 상품원가
    proddetailcode int , 
    vendorcode int , 
    primary key(wpcode) , 
    foreign key(proddetailcode) references productdetail(proddetailcode)
    on delete cascade on update cascade , 
    foreign key(vendorcode) references vendor(vendorcode)
    on delete cascade on update cascade
   );

drop table if exists polog;
create table polog(
	pocode int auto_increment , 					# 발주코드PK
	wpcode int ,  					
	quantity int not null , 						# 주문수량
	totalamount int , 								# 주문금액 주문수량 * 도매가
	quantitydate date default (current_date) , 		# 주문 날짜
	arrivaldate date , 								# 도착날짜
    quantitystate int default 1, 							# 처리상태
    primary key(pocode) , 
    foreign key(wpcode) references wholesaleproduct(wpcode)
	on delete cascade on update cascade 
   );


# 샘플

# color
insert into color(colorname) values('하얀색');
insert into color(colorname) values('검정색');
insert into color(colorname) values('하늘색');
insert into color(colorname) values('군청색');
insert into color(colorname) values('노란색');

# productcategory
insert into productcategory(prodcatename) values('티셔츠');
insert into productcategory(prodcatename) values('청바지');
insert into productcategory(prodcatename) values('모자');
insert into productcategory(prodcatename) values('양말');
insert into productcategory(prodcatename) values('장갑');

# product
insert into product(prodname, prodprice, prodgender, proddesc) values ("반팔티1", 10000, 'M', '티셔츠설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("장갑1", 10000, 'M', '장갑설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("양말1", 15000, 'U', '양말설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("모자1", 20000, 'F', '모자설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("청바지1", 22000, 'F', '청바지설명');

# productdetail
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (1, 1, 1, 'S', "반팔티1c1.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (2, 5, 2, 'M', "장갑1c2.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (3, 4, 2, 'L', "양말1c2.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (4, 3, 3, 'M', "모자1c3.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (5, 2, 4, 'XXL', "청바지1c4.png", "2022-08-01");

# 거래처
insert into vendor(vname , vcontact , vaddress) values('시크보그무역' , '02-1111-1111' , '서울시 종로구 청계천로 500, 3층');
insert into vendor(vname , vcontact , vaddress) values('스타일스피어' , '02-2222-2222' , '경기도 고양시 일산동구 중앙로 321, 2층');
insert into vendor(vname , vcontact , vaddress) values('엘레강스엠포리엄' , '02-3333-3333' , '부산시 부산진구 부전로 654, 5층');
insert into vendor(vname , vcontact , vaddress) values('트렌드아우라' , '02-4444-4444' , '광주시 서구 상무대로 789, 4층');
insert into vendor(vname , vcontact , vaddress) values('럭소라' , '02-5555-5555' , '대구시 중구 동성로 987, 6층');
INSERT INTO vendor (vname, vcontact, vaddress)
VALUES ('에코테크', '02-6666-6666', '서울시 강남구 테헤란로 123, 5층');
INSERT INTO vendor (vname, vcontact, vaddress)
VALUES ('하이테크 솔루션', '02-7777-77777', '서울시 마포구 월드컵로 456, 2층');
INSERT INTO vendor (vname, vcontact, vaddress)
VALUES ('그린산업', '02-8888-88888', '서울시 송파구 올림픽로 789, 4층');
INSERT INTO vendor (vname, vcontact, vaddress)
VALUES ('스타트업 코리아', '02-9999-9999', '서울시 서초구 서초대로 101, 6층');
INSERT INTO vendor (vname, vcontact, vaddress)
VALUES ('피닉스 파트너스', '02-0000-0000', '서울시 영등포구 경인로 102, 7층');

select * from vendor;

# 도매상품
insert into wholesaleproduct(wpname , wpcost , proddetailcode , vendorcode) values('반팔-WH-S' , 7000 , 1, 1);
insert into wholesaleproduct(wpname , wpcost , proddetailcode , vendorcode) values('반팔-WH-M' , 7000 , 2, 1);
insert into wholesaleproduct(wpname , wpcost , proddetailcode , vendorcode) values('양말-BL' , 5000 , 3, 2);
insert into wholesaleproduct(wpname , wpcost , proddetailcode , vendorcode) values('모자-WH' , 10000 , 4, 4);
insert into wholesaleproduct(wpname , wpcost , proddetailcode , vendorcode) values('청바지-BLUE' , 18000 , 5, 5);

select * from wholesaleproduct;

# 발주로그
insert into polog(wpcode , quantity , totalamount , arrivaldate , quantitystate) values(1 , 3 , 21000 , '2024-08-16' , 1);
insert into polog(wpcode , quantity , totalamount , quantitystate) values(2 , 5 , 35000 , 1);
insert into polog(wpcode , quantity , totalamount , arrivaldate , quantitystate) values(3 , 2 , 10000 , '2024-08-14' , 2);
insert into polog(wpcode , quantity , totalamount , arrivaldate , quantitystate) values(4 , 3 , 30000 , '2024-08-14' , 2);
insert into polog(wpcode , quantity , totalamount , arrivaldate , quantitystate) values(5 , 3 , 54000 , '2024-08-14' , 2);
select * from polog;

# members
insert into members(memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('유재석', '010-1111-1111', 'you@naver.com', 'M', '1', 'M', '2022-08-01');
insert into members(memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('강호동', '010-2222-2222', 'gang@naver.com', 'M', '1', 'XL', '2022-08-05');
insert into members(memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('하하', '010-3333-3333', 'haha@naver.com', 'F', '2', 'S', '2023-04-08');
insert into members(memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('신동엽', '010-4444-4444', 'god@naver.com', 'M', '2', 'S', '2023-05-27');
insert into members(memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('서장훈', '010-5555-5555', '280m@naver.com', 'M', '3', 'XL', '2024-08-01');

# coupon
insert into coupon(coupname, coupsalerate, memcode, coupexpdate) values ('VIP10', 10, 1, '2024-09-30');
insert into coupon(coupname, coupsalerate, memcode, coupexpdate) values ('VIP9', 50, 1, '2024-09-30');
insert into coupon(coupname, coupsalerate, memcode, coupexpdate) values ('HELLO', 80, 1, '2024-09-30');
insert into coupon(coupname, coupsalerate, memcode, coupexpdate) values ('WORLD', 60, 1, '2024-09-30');
insert into coupon(coupname, coupsalerate, memcode, coupexpdate) values ('TEMU', 10, 1, '2024-09-30');

# orders
insert into orders(memcode, orddate) values (1, "2024-07-31");
insert into orders(memcode, orddate) values (1, "2024-07-31");
insert into orders(memcode, orddate) values (1, "2024-07-31");
insert into orders(memcode, orddate) values (2, "2024-07-31");
insert into orders(memcode, orddate) values (2, "2024-07-31");

# orderdetail
insert into orderdetail(ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 1, 1, 1, 1, 10000);
insert into orderdetail(ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 2, 2, 2, 2, 10000);
insert into orderdetail(ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 3, 1, 3, 3, 10000);
insert into orderdetail(ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (2, 3, 1, 4, 1, 10000);
insert into orderdetail(ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (2, 4, 3, 5, 2, 10000);

# invlog
insert into invlog(proddetailcode, invlogchange, invlogdetail) values (1, 10, 1);
insert into invlog(proddetailcode, invlogchange, invlogdetail) values (1, -2, 2);
insert into invlog(proddetailcode, invlogchange, invlogdetail) values (1, 2, 4);
insert into invlog(proddetailcode, invlogchange, invlogdetail) values (2, 10, 1);
insert into invlog(proddetailcode, invlogchange, invlogdetail) values (2, -3, 2);

# support
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(1, 1, '상담1', '반품문의',	'2024-07-31', null, 1, 1);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(2, 2, '상담2', '반품문의',	'2024-07-31', 1, 2, null);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(3, 2, '상담3', '반품문의',	'2024-07-31', 2, 3, null);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(4, 3, '상담4', '교환문의',	'2024-07-31', null, 1, 3);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(5, 4, '상담5', '나문희',	'2024-07-31', null, 1, 5);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(1, 1, '상담1', '반품문의',	'2024-07-31', null, 1, 1);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(3, 2, '상담3', '반품문의',	'2024-07-31', 2, 1, null);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(2, 2, '상담3', '반품문의',	'2024-07-31', 2, 1, null);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(2, 2, '상담3', '반품문의',	'2024-07-31', 2, 1, null);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(2, 2, '상담3', '반품문의',	'2024-07-31', 2, 1, null);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(2, 2, '상담3', '반품문의',	'2024-07-31', 2, 1, null);
insert into support(memcode, supcategory, suptitle, supcontent, supdate, proddetailcode, supstate, ordcode) values(2, 2, '상담3', '반품문의',	'2024-07-31', 2, 1, null);

select * from support;

# reply
insert into reply(supcode, replycontent, replydate) values(1, '답글1', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(2, '답글2', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(3, '답글3', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(4, '답글4', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(5, '답글5', '2024-07-31');
select * from reply;

# admin
insert into admin(adminid, adminpw) values ('qwe123', 'qwe123');
insert into admin(adminid, adminpw) values ('asd456', 'asd456');
insert into admin(adminid, adminpw) values ('zxc789', 'zxc789');
insert into admin(adminid, adminpw) values ('rty012', 'rty012');
insert into admin(adminid, adminpw) values ('fgh345', 'fgh345');
select * from product;
select * from productdetail;
select * from color;
select * from productcategory;

# mockaroo 추가 샘플

# color
insert into color (colorname) values ('빨강색');
insert into color (colorname) values ('파랑색');
insert into color (colorname) values ('초록색');
insert into color (colorname) values ('노랑색');
insert into color (colorname) values ('주황색');
insert into color (colorname) values ('보라색');
insert into color (colorname) values ('분홍색');
insert into color (colorname) values ('갈색');
insert into color (colorname) values ('회색');
insert into color (colorname) values ('청록색');
insert into color (colorname) values ('자주색');
insert into color (colorname) values ('베이지색');
insert into color (colorname) values ('금색');
insert into color (colorname) values ('은색');
insert into color (colorname) values ('올리브색');

# members
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김태희', '010-7555-7531', 'vcrush0@joomla.org', 'M', 13, 'XXL', '2023-12-12');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이병헌', '010-2132-1327', 'gsainz1@trellian.com', 'F', 8, 'S', '2022-05-03');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('송혜교', '010-7841-5259', 'krugieri2@google.ca', 'M', 12, 'S', '2023-08-08');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('현빈', '010-6447-4162', 'jsherlaw3@purevolume.com', 'F', 16, 'S', '2022-05-13');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('한지민', '010-7702-2845', 'hlangland4@washington.edu', 'M', 11, 'M', '2023-10-22');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('공효진', '010-4586-9843', 'mbierman5@icq.com', 'M', 14, 'XXL', '2023-10-07');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('조인성', '010-9551-7991', 'mmcvity6@csmonitor.com', 'F', 6, 'L', '2023-07-19');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김수현', '010-1893-4239', 'ahinkes7@tamu.edu', 'F', 4, 'M', '2023-04-25');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('박보검', '010-7799-3466', 'pbyrnes8@deliciousdays.com', 'F', 15, 'S', '2021-03-02');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('수지', '010-1450-3720', 'csuthren9@symantec.com', 'M', 12, 'M', '2023-04-13');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이민호', '010-3598-1757', 'hblazia@addthis.com', 'F', 7, 'XL', '2021-11-20');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김유정', '010-7556-8117', 'eregib@google.com.br', 'F', 4, 'XXL', '2023-11-30');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('정해인', '010-7989-8751', 'jcoggellc@geocities.jp', 'F', 12, 'M', '2021-07-31');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김고은', '010-5549-5179', 'yderbyd@ucoz.com', 'M', 13, 'XL', '2022-08-23');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이동욱', '010-5213-6525', 'cbanfille@amazonaws.com', 'M', 7, 'XXL', '2023-09-11');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('박서준', '010-7314-5562', 'nfulhamf@nsw.gov.au', 'F', 11, 'M', '2023-10-20');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김래원', '010-6947-6883', 'mgallawayg@narod.ru', 'M', 19, 'L', '2023-12-23');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김남길', '010-6344-4600', 'lbarleeh@umn.edu', 'F', 2, 'M', '2023-12-12');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('한효주', '010-2470-4333', 'nbahiai@tuttocitta.it', 'M', 4, 'XXL', '2021-10-19');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이준기', '010-2984-8108', 'cwicksteadj@1688.com', 'M', 13, 'S', '2024-04-27');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김지원', '010-8125-4750', 'ndowdeswellk@mtv.com', 'M', 1, 'XL', '2023-08-11');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('박해일', '010-7396-5391', 'njacobsonl@usa.gov', 'M', 13, 'M', '2021-08-19');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김태리', '010-7333-8496', 'jarmanm@jalbum.net', 'M', 8, 'XXL', '2022-03-21');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('윤시윤', '010-1314-3760', 'iborsnalln@pen.io', 'F', 11, 'M', '2022-06-09');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이성민', '010-1542-1592', 'rpavelino@mit.edu', 'F', 6, 'XL', '2023-01-26');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김희철', '010-7529-7575', 'fsidep@acquirethisname.com', 'F', 6, 'L', '2022-12-27');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이영하', '010-9649-1404', 'cfilipyevq@myspace.com', 'F', 3, 'XXL', '2021-06-17');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이종석', '010-7193-9543', 'rzanollir@live.com', 'M', 2, 'S', '2022-11-06');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김소현', '010-4841-9710', 'dsapps@cafepress.com', 'M', 8, 'M', '2023-09-03');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('유연석', '010-6001-1110', 'ebutent@usa.gov', 'F', 5, 'M', '2022-05-18');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김혜수', '010-9736-8921', 'adalessiou@prnewswire.com', 'M', 19, 'S', '2022-12-24');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('정려원', '010-4221-7426', 'tpleagerv@networkadvertising.org', 'F', 1, 'XL', '2022-03-13');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이유영', '010-2953-5880', 'taylwinw@eepurl.com', 'F', 9, 'XL', '2023-08-06');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이하늬', '010-7392-7128', 'obeddiex@blogspot.com', 'F', 10, 'M', '2022-06-15');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김민준', '010-3963-6014', 'mspilletty@oracle.com', 'F', 13, 'S', '2021-01-01');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이상윤', '010-4523-1493', 'vbarukhz@fastcompany.com', 'F', 8, 'M', '2022-02-20');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이시언', '010-2980-3773', 'lkempe10@amazon.co.jp', 'F', 17, 'XXL', '2021-09-24');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김민희', '010-7538-8093', 'gtrickey11@flavors.me', 'F', 19, 'L', '2022-08-24');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이준', '010-7144-8966', 'ledeson12@theatlantic.com', 'F', 2, 'L', '2021-09-13');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김성령', '010-6245-2183', 'drobens13@china.com.cn', 'F', 6, 'XXL', '2021-11-30');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이유비', '010-9147-8260', 'mrickett14@i2i.jp', 'F', 1, 'XL', '2022-07-06');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이종혁', '010-5232-4611', 'kcecil15@unblog.fr', 'F', 16, 'XL', '2023-07-18');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('김재중', '010-9456-7455', 'aplante16@g.co', 'F', 1, 'XXL', '2021-03-01');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('이재영', '010-7778-8815', 'dmacguire17@geocities.com', 'M', 7, 'XXL', '2023-12-21');
insert into members (memname, memcontact, mememail, memgender, memcolor, memsize, memjoindate) values ('조권', '010-1562-5369', 'hkelling18@go.com', 'F', 10, 'M', '2023-11-05');

#color
insert into color (colorname) values ('빨강색');
insert into color (colorname) values ('파랑색');
insert into color (colorname) values ('초록색');
insert into color (colorname) values ('노랑색');
insert into color (colorname) values ('주황색');
insert into color (colorname) values ('보라색');
insert into color (colorname) values ('분홍색');
insert into color (colorname) values ('갈색');
insert into color (colorname) values ('회색');
insert into color (colorname) values ('청록색');
insert into color (colorname) values ('자주색');
insert into color (colorname) values ('베이지색');
insert into color (colorname) values ('금색');
insert into color (colorname) values ('은색');
insert into color (colorname) values ('올리브색');

# productcategory
insert into productcategory (prodcatename) values ('민소매');
insert into productcategory (prodcatename) values ('반바지');
insert into productcategory (prodcatename) values ('원피스');
insert into productcategory (prodcatename) values ('스웨터');
insert into productcategory (prodcatename) values ('재킷');
insert into productcategory (prodcatename) values ('썬캡');
insert into productcategory (prodcatename) values ('스커트');
insert into productcategory (prodcatename) values ('셔츠');
insert into productcategory (prodcatename) values ('점퍼');
insert into productcategory (prodcatename) values ('패딩');
insert into productcategory (prodcatename) values ('가디건');
insert into productcategory (prodcatename) values ('블라우스');
insert into productcategory (prodcatename) values ('조끼');
insert into productcategory (prodcatename) values ('장목양말');
insert into productcategory (prodcatename) values ('트레이닝복');

# product
insert into product (prodname, prodprice, prodgender, proddesc) values ('민소매1', 40000, 'M', '민소매1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('반바지1', 19000, 'F', '반바지1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('원피스1', 18000, 'F', '원피스1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('스웨터1', 19000, 'F', '스웨터1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('재킷1', 23000, 'U', '재킷1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('썬캡1', 18000, 'F', '썬캡1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('스커트1', 36000, 'F', '스커트1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('셔츠1', 60000, 'U', '셔츠1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('점퍼1', 32000, 'U', '점퍼1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('패딩1', 17000, 'M', '패딩1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('가디건1', 59000, 'F', '가디건1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('블라우스1', 49000, 'F', '블라우스1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('조끼1', 19000, 'M', '조끼1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('장목양말1', 44000, 'M', '장목양말1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('트레이닝복1', 42000, 'M', '트레이닝복1설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('민소매2', 35000, 'F', '민소매2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('반바지2', 28000, 'M', '반바지2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('원피스2', 60000, 'F', '원피스2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('스웨터2', 26000, 'M', '스웨터2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('재킷2', 30000, 'M', '재킷2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('썬캡2', 29000, 'U', '썬캡2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('스커트2', 32000, 'F', '스커트2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('셔츠2', 17000, 'M', '셔츠2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('점퍼2', 35000, 'M', '점퍼2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('패딩2', 49000, 'M', '패딩2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('가디건2', 40000, 'F', '가디건2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('블라우스2', 36000, 'F', '블라우스2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('조끼2', 50000, 'M', '조끼2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('장목양말2', 15000, 'U', '장목양말2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('트레이닝복2', 40000, 'F', '트레이닝복2설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('민소매3', 19000, 'M', '민소매3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('반바지3', 42000, 'F', '반바지3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('원피스3', 60000, 'F', '원피스3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('스웨터3', 27000, 'U', '스웨터3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('재킷3', 23000, 'U', '재킷3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('썬캡3', 38000, 'U', '썬캡3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('스커트3', 17000, 'F', '스커트3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('셔츠3', 52000, 'U', '셔츠3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('점퍼3', 50000, 'F', '점퍼3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('패딩3', 16000, 'M', '패딩3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('가디건3', 59000, 'U', '가디건3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('블라우스3', 27000, 'F', '블라우스3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('조끼3', 17000, 'F', '조끼3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('장목양말3', 49000, 'U', '장목양말3설명');
insert into product (prodname, prodprice, prodgender, proddesc) values ('트레이닝복3', 23000, 'F', '트레이닝복3설명');

# productdetail
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 5, 'S', '민소매c5.png', '2023-03-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 12, 'M', '민소매c12.png', '2022-04-25');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 3, 'L', '민소매c3.png', '2021-03-10');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 17, 'XL', '민소매c17.png', '2022-01-28');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 8, 'XXL', '민소매c8.png', '2023-12-17');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 14, 'S', '민소매c14.png', '2023-07-20');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 1, 'M', '민소매c1.png', '2023-06-11');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 19, 'L', '민소매c19.png', '2023-01-07');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 6, 'XL', '민소매c6.png', '2022-07-28');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (6, 6, 10, 'XXL', '민소매c10.png', '2023-02-27');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 14, 'S', '반바지c14.png', '2023-02-19');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 6, 'S', '반바지c6.png', '2021-08-20');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 4, 'M', '반바지c4.png', '2023-01-09');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 5, 'L', '반바지c5.png', '2021-07-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 17, 'XL', '반바지c17.png', '2023-09-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 3, 'XXL', '반바지c3.png', '2023-04-20');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 10, 'S', '반바지c10.png', '2022-08-07');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 5, 'M', '반바지c5.png', '2021-11-20');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 1, 'L', '반바지c1.png', '2022-09-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 2, 'XL', '반바지c2.png', '2021-07-11');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (7, 7, 13, 'XXL', '반바지c13.png', '2021-03-10');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 5, 'S', '원피스c5.png', '2023-07-09');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 12, 'M', '원피스c12.png', '2022-09-22');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 3, 'L', '원피스c3.png', '2023-03-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 17, 'XL', '원피스c17.png', '2023-02-28');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 8, 'XXL', '원피스c8.png', '2021-03-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 14, 'S', '원피스c14.png', '2023-11-25');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 2, 'M', '원피스c2.png', '2021-05-17');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 19, 'L', '원피스c19.png', '2023-01-30');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 10, 'XL', '원피스c10.png', '2023-04-26');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (8, 8, 6, 'XXL', '원피스c6.png', '2021-01-11');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 5, 'S', '스웨터c5.png', '2022-05-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 12, 'M', '스웨터c12.png', '2021-08-12');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 8, 'L', '스웨터c8.png', '2021-07-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 17, 'XL', '스웨터c17.png', '2023-02-17');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 3, 'XXL', '스웨터c3.png', '2023-11-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 10, 'S', '스웨터c10.png', '2021-05-09');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 14, 'M', '스웨터c14.png', '2022-06-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 1, 'L', '스웨터c1.png', '2022-09-16');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 6, 'XL', '스웨터c6.png', '2022-07-16');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (9, 9, 19, 'XXL', '스웨터c19.png', '2023-01-22');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 5, 'S', '재킷c5.png', '2021-06-09');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 12, 'M', '재킷c12.png', '2023-03-02');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 8, 'L', '재킷c8.png', '2021-04-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 17, 'XL', '재킷c17.png', '2022-09-21');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 3, 'XXL', '재킷c3.png', '2023-08-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 10, 'S', '재킷c10.png', '2023-02-05');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 14, 'M', '재킷c14.png', '2023-03-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 1, 'L', '재킷c1.png', '2022-07-29');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 19, 'XL', '재킷c19.png', '2021-06-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (10, 10, 6, 'XXL', '재킷c6.png', '2021-02-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 5, 'S', '썬캡c5.png', '2022-04-10');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 12, 'M', '썬캡c12.png', '2021-05-30');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 8, 'L', '썬캡c8.png', '2021-07-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 17, 'XL', '썬캡c17.png', '2021-09-30');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 3, 'XXL', '썬캡c3.png', '2021-10-13');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 10, 'S', '썬캡c10.png', '2021-09-10');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 14, 'M', '썬캡c14.png', '2022-04-08');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 1, 'L', '썬캡c1.png', '2021-10-05');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 19, 'XL', '썬캡c19.png', '2022-03-27');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (11, 11, 6, 'XXL', '썬캡c6.png', '2023-12-09');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 5, 'S', '스커트c5.png', '2021-01-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 12, 'M', '스커트c12.png', '2021-05-29');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 3, 'L', '스커트c3.png', '2021-10-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 18, 'XL', '스커트c18.png', '2023-10-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 7, 'XXL', '스커트c7.png', '2022-04-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 14, 'S', '스커트c14.png', '2023-01-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 9, 'M', '스커트c9.png', '2021-11-10');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 2, 'L', '스커트c2.png', '2022-05-29');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 16, 'XL', '스커트c16.png', '2024-01-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (12, 12, 11, 'XXL', '스커트c11.png', '2023-09-17');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 5, 'S', '셔츠c5.png', '2021-08-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 12, 'M', '셔츠c12.png', '2021-08-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 3, 'L', '셔츠c3.png', '2021-03-18');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 17, 'XL', '셔츠c17.png', '2021-09-24');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 8, 'XXL', '셔츠c8.png', '2022-01-05');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 10, 'S', '셔츠c10.png', '2022-12-25');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 1, 'M', '셔츠c1.png', '2022-03-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 19, 'L', '셔츠c19.png', '2022-12-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 6, 'XL', '셔츠c6.png', '2023-08-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (13, 13, 14, 'XXL', '셔츠c14.png', '2021-05-09');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 5, 'S', '점퍼c5.png', '2024-01-29');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 12, 'M', '점퍼c12.png', '2022-01-18');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 3, 'L', '점퍼c3.png', '2021-04-19');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 18, 'XL', '점퍼c18.png', '2022-10-21');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 7, 'XXL', '점퍼c7.png', '2023-05-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 14, 'S', '점퍼c14.png', '2021-09-02');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 1, 'M', '점퍼c1.png', '2023-07-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 9, 'L', '점퍼c9.png', '2024-01-09');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 10, 'XL', '점퍼c10.png', '2023-06-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (14, 14, 20, 'XXL', '점퍼c20.png', '2021-01-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 5, 'S', '패딩c5.png', '2023-06-11');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 12, 'M', '패딩c12.png', '2023-03-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 3, 'L', '패딩c3.png', '2022-01-17');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 18, 'XL', '패딩c18.png', '2022-02-08');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 7, 'XXL', '패딩c7.png', '2022-01-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 14, 'S', '패딩c14.png', '2023-02-07');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 1, 'M', '패딩c1.png', '2023-10-25');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 9, 'L', '패딩c9.png', '2022-12-21');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 10, 'XL', '패딩c10.png', '2022-02-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (15, 15, 20, 'XXL', '패딩c20.png', '2022-06-27');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 5, 'S', '가디건c5.png', '2023-12-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 12, 'M', '가디건c12.png', '2023-11-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 3, 'L', '가디건c3.png', '2023-11-17');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 18, 'XL', '가디건c18.png', '2022-08-12');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 7, 'XXL', '가디건c7.png', '2021-11-22');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 14, 'S', '가디건c14.png', '2022-06-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 9, 'M', '가디건c9.png', '2021-12-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 2, 'L', '가디건c2.png', '2021-02-08');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 16, 'XL', '가디건c16.png', '2023-04-07');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (16, 16, 10, 'XXL', '가디건c10.png', '2022-12-04');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 5, 'S', '블라우스c5.png', '2021-10-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 12, 'M', '블라우스c12.png', '2021-03-18');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 3, 'L', '블라우스c3.png', '2024-01-05');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 17, 'XL', '블라우스c17.png', '2023-11-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 8, 'XXL', '블라우스c8.png', '2022-01-10');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 10, 'S', '블라우스c10.png', '2021-09-05');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 1, 'M', '블라우스c1.png', '2022-03-21');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 14, 'L', '블라우스c14.png', '2023-02-07');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 6, 'XL', '블라우스c6.png', '2021-09-22');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (17, 17, 19, 'XXL', '블라우스c19.png', '2021-03-19');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 5, 'S', '조끼c5.png', '2021-07-27');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 12, 'M', '조끼c12.png', '2021-01-18');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 8, 'L', '조끼c8.png', '2022-04-24');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 3, 'XL', '조끼c3.png', '2023-07-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 17, 'XXL', '조끼c17.png', '2023-06-04');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 10, 'S', '조끼c10.png', '2023-04-11');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 1, 'M', '조끼c1.png', '2022-09-26');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 6, 'L', '조끼c6.png', '2022-09-18');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 14, 'XL', '조끼c14.png', '2023-07-15');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (18, 18, 19, 'XXL', '조끼c19.png', '2021-08-19');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 2, 'S', '장목양말c2.png', '2021-05-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 4, 'M', '장목양말c4.png', '2021-07-18');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 6, 'L', '장목양말c6.png', '2021-11-06');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 8, 'XL', '장목양말c8.png', '2021-11-18');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 10, 'XXL', '장목양말c10.png', '2021-10-02');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 12, 'S', '장목양말c12.png', '2023-06-07');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 14, 'M', '장목양말c14.png', '2022-11-01');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 16, 'L', '장목양말c16.png', '2022-04-12');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 18, 'XL', '장목양말c18.png', '2024-01-10');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (19, 19, 20, 'XXL', '장목양말c20.png', '2023-04-12');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 2, 'S', '트레이닝복c2.png', '2021-12-03');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 4, 'M', '트레이닝복c4.png', '2021-03-16');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 6, 'L', '트레이닝복c6.png', '2023-05-02');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 8, 'XL', '트레이닝복c8.png', '2023-10-02');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 10, 'XXL', '트레이닝복c10.png', '2024-01-22');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 12, 'S', '트레이닝복c12.png', '2023-02-08');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 14, 'M', '트레이닝복c14.png', '2021-09-25');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 16, 'L', '트레이닝복c16.png', '2023-11-21');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 18, 'XL', '트레이닝복c18.png', '2023-12-23');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (20, 20, 20, 'XXL', '트레이닝복c20.png', '2022-08-20');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 8, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 6, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 2, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 18, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 10, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 1, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 19, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 16, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 11, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (21, 6, 3, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 12, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 3, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 6, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 13, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 2, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 18, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 19, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 15, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 11, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (22, 7, 5, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 1, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 13, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 7, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 17, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 10, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 6, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 11, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 19, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 20, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (23, 8, 14, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 19, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 20, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 13, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 15, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 3, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 4, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 18, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 16, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 6, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (24, 9, 8, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 20, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 14, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 1, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 6, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 7, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 11, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 17, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 4, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 9, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (25, 10, 12, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 1, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 7, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 8, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 12, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 9, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 16, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 19, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 20, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (26, 11, 4, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 5, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 1, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 13, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 7, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 12, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 4, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 14, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 20, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 3, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (27, 12, 8, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 12, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 20, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 19, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 16, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 5, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 10, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 8, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 11, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 18, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (28, 13, 14, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 6, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 13, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 17, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 4, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 20, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 10, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 19, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 18, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (29, 14, 16, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 12, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 16, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 7, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 17, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 11, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 3, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 9, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 6, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 1, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (30, 15, 4, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 16, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 2, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 8, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 1, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 20, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 18, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 9, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 19, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 7, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (31, 16, 11, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 1, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 2, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 14, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 15, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 10, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 16, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 9, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 4, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (32, 17, 20, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 15, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 2, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 3, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 11, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 5, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 14, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 1, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 12, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 20, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (33, 18, 6, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 12, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 13, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 17, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 2, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 19, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 20, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 10, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 7, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (34, 19, 3, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 17, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 1, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 19, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 16, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 10, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 2, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 13, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 9, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 15, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (35, 20, 4, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 3, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 4, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 11, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 6, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 14, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 18, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 16, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 8, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 10, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (36, 6, 9, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 1, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 6, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 9, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 18, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 15, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 20, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 19, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 10, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 17, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (37, 7, 12, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 8, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 7, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 9, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 10, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 12, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 13, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 6, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 17, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 15, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (38, 8, 20, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 11, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 12, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 6, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 20, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 10, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 19, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 16, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 15, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (39, 9, 1, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 19, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 18, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 3, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 6, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 13, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 2, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 20, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 12, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 9, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (40, 10, 15, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 15, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 2, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 13, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 14, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 3, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 17, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 8, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 4, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 1, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (41, 11, 16, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 17, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 11, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 7, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 20, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 6, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 8, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 16, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 9, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (42, 12, 4, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 11, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 10, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 15, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 3, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 4, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 17, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 13, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 5, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 6, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (43, 13, 2, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 9, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 15, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 14, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 10, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 13, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 2, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 17, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 8, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 6, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (44, 14, 19, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 9, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 6, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 17, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 1, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 11, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 4, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 18, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 14, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (45, 15, 7, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 2, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 18, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 13, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 9, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 16, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 17, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 3, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 5, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 20, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (46, 16, 11, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 13, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 9, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 5, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 4, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 19, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 12, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 11, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 2, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 10, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (47, 17, 14, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 19, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 13, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 6, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 10, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 5, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 2, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 15, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 14, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 8, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (48, 18, 11, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 20, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 19, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 17, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 1, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 6, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 11, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 16, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 5, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 10, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (49, 19, 13, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 15, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 5, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 16, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 11, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 7, 'XXL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 19, 'S');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 4, 'M');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 8, 'L');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 2, 'XL');
insert into productdetail (prodcode, prodcatecode, colorcode, prodsize) values (50, 20, 12, 'XXL');


#orders
insert into orders (memcode, orddate) values (4, '2023-12-19');
insert into orders (memcode, orddate) values (4, '2022-10-29');
insert into orders (memcode, orddate) values (1, '2023-11-22');
insert into orders (memcode, orddate) values (1, '2023-04-16');
insert into orders (memcode, orddate) values (4, '2021-12-18');
insert into orders (memcode, orddate) values (2, '2022-12-06');
insert into orders (memcode, orddate) values (5, '2023-05-30');
insert into orders (memcode, orddate) values (1, '2023-09-02');
insert into orders (memcode, orddate) values (3, '2023-04-23');
insert into orders (memcode, orddate) values (1, '2023-10-10');
insert into orders (memcode, orddate) values (5, '2024-01-31');
insert into orders (memcode, orddate) values (4, '2021-07-26');
insert into orders (memcode, orddate) values (3, '2022-07-23');
insert into orders (memcode, orddate) values (1, '2022-09-22');
insert into orders (memcode, orddate) values (4, '2022-04-12');
insert into orders (memcode, orddate) values (1, '2021-08-20');
insert into orders (memcode, orddate) values (2, '2021-11-15');
insert into orders (memcode, orddate) values (3, '2023-02-15');
insert into orders (memcode, orddate) values (5, '2021-11-24');
insert into orders (memcode, orddate) values (3, '2023-08-24');
insert into orders (memcode, orddate) values (4, '2021-03-29');
insert into orders (memcode, orddate) values (5, '2023-09-05');
insert into orders (memcode, orddate) values (1, '2022-01-02');
insert into orders (memcode, orddate) values (5, '2021-09-25');
insert into orders (memcode, orddate) values (1, '2023-01-08');
insert into orders (memcode, orddate) values (2, '2024-04-04');
insert into orders (memcode, orddate) values (3, '2022-11-15');
insert into orders (memcode, orddate) values (1, '2021-08-09');
insert into orders (memcode, orddate) values (3, '2023-12-11');
insert into orders (memcode, orddate) values (3, '2023-06-17');
insert into orders (memcode, orddate) values (2, '2021-10-26');
insert into orders (memcode, orddate) values (1, '2023-08-15');
insert into orders (memcode, orddate) values (1, '2022-07-03');
insert into orders (memcode, orddate) values (4, '2024-01-14');
insert into orders (memcode, orddate) values (1, '2024-05-14');
insert into orders (memcode, orddate) values (2, '2021-09-24');
insert into orders (memcode, orddate) values (2, '2021-06-13');
insert into orders (memcode, orddate) values (5, '2024-01-13');
insert into orders (memcode, orddate) values (1, '2022-03-22');
insert into orders (memcode, orddate) values (4, '2021-06-25');
insert into orders (memcode, orddate) values (4, '2023-01-03');
insert into orders (memcode, orddate) values (3, '2023-04-26');
insert into orders (memcode, orddate) values (2, '2021-12-01');
insert into orders (memcode, orddate) values (4, '2024-02-14');
insert into orders (memcode, orddate) values (3, '2021-09-28');
insert into orders (memcode, orddate) values (1, '2021-03-21');
insert into orders (memcode, orddate) values (1, '2021-03-29');
insert into orders (memcode, orddate) values (1, '2023-04-22');
insert into orders (memcode, orddate) values (3, '2021-04-12');
insert into orders (memcode, orddate) values (3, '2021-04-14');
insert into orders (memcode, orddate) values (2, '2023-10-28');
insert into orders (memcode, orddate) values (4, '2021-06-27');
insert into orders (memcode, orddate) values (1, '2024-03-27');
insert into orders (memcode, orddate) values (5, '2021-09-23');
insert into orders (memcode, orddate) values (4, '2023-05-17');
insert into orders (memcode, orddate) values (4, '2023-05-08');
insert into orders (memcode, orddate) values (1, '2022-03-10');
insert into orders (memcode, orddate) values (5, '2024-05-23');
insert into orders (memcode, orddate) values (2, '2023-06-16');
insert into orders (memcode, orddate) values (3, '2021-07-06');
insert into orders (memcode, orddate) values (1, '2023-06-07');
insert into orders (memcode, orddate) values (1, '2022-08-18');
insert into orders (memcode, orddate) values (1, '2022-10-30');
insert into orders (memcode, orddate) values (1, '2021-06-12');
insert into orders (memcode, orddate) values (2, '2022-06-11');
insert into orders (memcode, orddate) values (4, '2024-04-15');
insert into orders (memcode, orddate) values (5, '2023-08-04');
insert into orders (memcode, orddate) values (1, '2022-08-31');
insert into orders (memcode, orddate) values (5, '2021-07-22');
insert into orders (memcode, orddate) values (2, '2022-12-09');
insert into orders (memcode, orddate) values (2, '2024-06-21');
insert into orders (memcode, orddate) values (3, '2023-12-25');
insert into orders (memcode, orddate) values (3, '2023-11-30');
insert into orders (memcode, orddate) values (4, '2024-01-29');
insert into orders (memcode, orddate) values (1, '2023-08-22');
insert into orders (memcode, orddate) values (3, '2024-03-29');
insert into orders (memcode, orddate) values (1, '2023-05-11');
insert into orders (memcode, orddate) values (5, '2022-04-01');
insert into orders (memcode, orddate) values (4, '2024-01-26');
insert into orders (memcode, orddate) values (3, '2022-07-19');
insert into orders (memcode, orddate) values (4, '2022-03-03');
insert into orders (memcode, orddate) values (4, '2022-07-11');
insert into orders (memcode, orddate) values (2, '2022-08-02');
insert into orders (memcode, orddate) values (1, '2023-08-31');
insert into orders (memcode, orddate) values (5, '2022-12-01');
insert into orders (memcode, orddate) values (1, '2022-06-23');
insert into orders (memcode, orddate) values (4, '2023-01-24');
insert into orders (memcode, orddate) values (1, '2022-07-29');
insert into orders (memcode, orddate) values (3, '2022-02-11');
insert into orders (memcode, orddate) values (4, '2022-05-10');
insert into orders (memcode, orddate) values (2, '2022-07-05');
insert into orders (memcode, orddate) values (3, '2023-11-14');
insert into orders (memcode, orddate) values (3, '2023-02-24');
insert into orders (memcode, orddate) values (2, '2022-01-11');
insert into orders (memcode, orddate) values (5, '2023-10-03');
insert into orders (memcode, orddate) values (3, '2022-01-03');
insert into orders (memcode, orddate) values (1, '2023-06-19');
insert into orders (memcode, orddate) values (4, '2023-05-04');
insert into orders (memcode, orddate) values (4, '2023-11-05');
insert into orders (memcode, orddate) values (3, '2023-05-31');

# orderdetail
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 18, 5, 5, 5, 63000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (2, 93, 3, 4, 4, 64000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 145, 7, 1, 3, 103000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (4, 70, 5, 1, 1, 32000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (5, 21, 6, 4, 4, 28000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (6, 40, 5, 3, 1, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (7, 136, 3, 5, 2, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (8, 92, 5, 4, 4, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (9, 9, 10, 2, 2, 39000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (10, 53, 3, 4, 5, 43000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (11, 149, 3, 1, 4, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (12, 50, 1, 5, 3, 59000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (13, 137, 8, 4, 4, 87000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (14, 77, 8, 3, 3, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (15, 2, 6, 1, 1, 46000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (16, 21, 2, 4, 3, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 10, 3, 2, 3, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (18, 138, 1, 1, 3, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (19, 80, 7, 3, 5, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (20, 93, 3, 2, 3, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (21, 49, 1, 4, 3, 35000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (22, 120, 9, 3, 5, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (23, 150, 5, 5, 4, 28000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (24, 43, 1, 3, 2, 99000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 8, 5, 2, 4, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (26, 51, 6, 4, 3, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (27, 95, 5, 4, 4, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (28, 63, 8, 1, 2, 85000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (29, 130, 10, 5, 2, 53000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (30, 97, 9, 4, 5, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (31, 132, 3, 1, 1, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (32, 14, 4, 2, 3, 29000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (33, 22, 8, 5, 4, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (34, 51, 2, 5, 3, 26000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (35, 58, 6, 3, 1, 78000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (36, 154, 2, 3, 2, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (37, 145, 9, 4, 4, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (38, 81, 9, 1, 2, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (39, 25, 9, 3, 3, 30000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (40, 149, 3, 2, 1, 46000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (41, 150, 2, 5, 2, 52000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (42, 2, 10, 4, 1, 82000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (43, 75, 8, 1, 3, 25000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 106, 8, 3, 1, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (45, 141, 9, 1, 2, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (46, 10, 1, 4, 4, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (47, 110, 4, 1, 2, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (48, 88, 8, 1, 3, 53000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (49, 71, 7, 4, 3, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 12, 8, 2, 5, 24000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (51, 83, 1, 5, 3, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (52, 69, 4, 1, 2, 44000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (53, 54, 4, 4, 1, 98000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (54, 33, 5, 2, 4, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (55, 124, 8, 2, 2, 55000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (56, 33, 7, 5, 1, 98000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (57, 55, 3, 2, 3, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (58, 42, 9, 3, 5, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (59, 4, 4, 1, 3, 103000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (60, 148, 5, 3, 5, 99000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (61, 151, 9, 5, 2, 109000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (62, 67, 8, 3, 4, 57000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (63, 36, 6, 2, 5, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (64, 143, 10, 2, 2, 99000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (65, 80, 1, 5, 3, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (66, 76, 9, 5, 2, 55000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (67, 138, 10, 5, 1, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (68, 134, 8, 3, 3, 52000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (69, 103, 10, 1, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (70, 141, 9, 2, 3, 106000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (71, 124, 10, 3, 2, 48000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (72, 46, 8, 4, 4, 28000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (73, 47, 4, 5, 3, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (74, 120, 8, 4, 1, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (75, 127, 10, 3, 2, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (76, 4, 5, 5, 1, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (77, 122, 9, 5, 3, 66000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (78, 117, 10, 5, 2, 31000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (79, 56, 9, 5, 5, 34000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (80, 76, 5, 5, 2, 93000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (81, 123, 10, 4, 1, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (82, 101, 8, 1, 4, 102000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (83, 118, 2, 2, 2, 94000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (84, 127, 4, 1, 5, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (85, 74, 10, 1, 4, 29000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (86, 97, 9, 5, 3, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 109, 9, 4, 4, 56000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (88, 6, 7, 3, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (89, 81, 5, 3, 3, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (90, 149, 8, 2, 2, 89000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (91, 92, 3, 1, 2, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (92, 38, 8, 5, 3, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (93, 22, 8, 1, 4, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (94, 91, 1, 3, 3, 34000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (95, 73, 10, 5, 2, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (96, 5, 8, 3, 5, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (97, 132, 9, 2, 3, 92000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (98, 150, 6, 1, 1, 98000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (99, 29, 6, 4, 4, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (100, 65, 7, 3, 5, 20000);

# /Mockaroo 샘플

# Mockaroo orders & orderdetail 더 추가

insert into orders (memcode, orddate) values (3, '2022-09-03');
insert into orders (memcode, orddate) values (15, '2022-11-10');
insert into orders (memcode, orddate) values (11, '2024-06-03');
insert into orders (memcode, orddate) values (1, '2021-08-15');
insert into orders (memcode, orddate) values (48, '2022-05-26');
insert into orders (memcode, orddate) values (13, '2021-02-03');
insert into orders (memcode, orddate) values (11, '2021-07-06');
insert into orders (memcode, orddate) values (47, '2023-06-14');
insert into orders (memcode, orddate) values (30, '2021-04-15');
insert into orders (memcode, orddate) values (47, '2023-12-25');
insert into orders (memcode, orddate) values (28, '2022-03-02');
insert into orders (memcode, orddate) values (44, '2021-03-27');
insert into orders (memcode, orddate) values (23, '2022-03-30');
insert into orders (memcode, orddate) values (31, '2022-08-22');
insert into orders (memcode, orddate) values (3, '2022-05-28');
insert into orders (memcode, orddate) values (31, '2024-02-27');
insert into orders (memcode, orddate) values (50, '2023-04-23');
insert into orders (memcode, orddate) values (26, '2023-09-30');
insert into orders (memcode, orddate) values (19, '2021-02-09');
insert into orders (memcode, orddate) values (39, '2022-12-11');
insert into orders (memcode, orddate) values (45, '2022-09-01');
insert into orders (memcode, orddate) values (26, '2023-12-29');
insert into orders (memcode, orddate) values (24, '2023-11-28');
insert into orders (memcode, orddate) values (9, '2023-07-20');
insert into orders (memcode, orddate) values (25, '2021-06-16');
insert into orders (memcode, orddate) values (18, '2023-11-05');
insert into orders (memcode, orddate) values (33, '2024-03-27');
insert into orders (memcode, orddate) values (49, '2023-02-10');
insert into orders (memcode, orddate) values (17, '2021-11-13');
insert into orders (memcode, orddate) values (18, '2024-06-17');
insert into orders (memcode, orddate) values (40, '2022-07-24');
insert into orders (memcode, orddate) values (2, '2022-09-27');
insert into orders (memcode, orddate) values (19, '2021-09-20');
insert into orders (memcode, orddate) values (16, '2023-02-08');
insert into orders (memcode, orddate) values (1, '2024-05-14');
insert into orders (memcode, orddate) values (29, '2023-05-01');
insert into orders (memcode, orddate) values (9, '2022-04-14');
insert into orders (memcode, orddate) values (41, '2021-07-12');
insert into orders (memcode, orddate) values (3, '2022-01-08');
insert into orders (memcode, orddate) values (40, '2022-06-17');
insert into orders (memcode, orddate) values (33, '2022-11-24');
insert into orders (memcode, orddate) values (36, '2022-04-02');
insert into orders (memcode, orddate) values (44, '2022-11-05');
insert into orders (memcode, orddate) values (15, '2024-01-22');
insert into orders (memcode, orddate) values (50, '2024-03-19');
insert into orders (memcode, orddate) values (43, '2022-09-05');
insert into orders (memcode, orddate) values (13, '2022-04-06');
insert into orders (memcode, orddate) values (43, '2021-05-02');
insert into orders (memcode, orddate) values (36, '2021-05-13');
insert into orders (memcode, orddate) values (32, '2024-03-27');
insert into orders (memcode, orddate) values (2, '2024-01-02');
insert into orders (memcode, orddate) values (11, '2023-03-20');
insert into orders (memcode, orddate) values (44, '2022-05-17');
insert into orders (memcode, orddate) values (15, '2021-06-22');
insert into orders (memcode, orddate) values (17, '2024-05-15');
insert into orders (memcode, orddate) values (21, '2023-07-20');
insert into orders (memcode, orddate) values (16, '2022-04-16');
insert into orders (memcode, orddate) values (42, '2022-01-14');
insert into orders (memcode, orddate) values (18, '2021-07-24');
insert into orders (memcode, orddate) values (36, '2023-09-08');
insert into orders (memcode, orddate) values (3, '2021-10-25');
insert into orders (memcode, orddate) values (38, '2021-11-10');
insert into orders (memcode, orddate) values (10, '2021-06-07');
insert into orders (memcode, orddate) values (26, '2021-02-22');
insert into orders (memcode, orddate) values (24, '2021-01-05');
insert into orders (memcode, orddate) values (48, '2022-01-19');
insert into orders (memcode, orddate) values (19, '2022-09-13');
insert into orders (memcode, orddate) values (44, '2023-02-08');
insert into orders (memcode, orddate) values (28, '2023-08-09');
insert into orders (memcode, orddate) values (22, '2022-12-07');
insert into orders (memcode, orddate) values (47, '2024-01-19');
insert into orders (memcode, orddate) values (26, '2023-09-23');
insert into orders (memcode, orddate) values (17, '2023-02-14');
insert into orders (memcode, orddate) values (36, '2023-03-16');
insert into orders (memcode, orddate) values (49, '2022-11-27');
insert into orders (memcode, orddate) values (15, '2022-11-03');
insert into orders (memcode, orddate) values (14, '2024-06-13');
insert into orders (memcode, orddate) values (1, '2021-05-12');
insert into orders (memcode, orddate) values (20, '2021-04-05');
insert into orders (memcode, orddate) values (23, '2023-01-31');
insert into orders (memcode, orddate) values (42, '2022-08-22');
insert into orders (memcode, orddate) values (49, '2022-07-14');
insert into orders (memcode, orddate) values (40, '2021-11-22');
insert into orders (memcode, orddate) values (18, '2024-08-06');
insert into orders (memcode, orddate) values (31, '2023-03-19');
insert into orders (memcode, orddate) values (4, '2022-11-27');
insert into orders (memcode, orddate) values (21, '2023-12-13');
insert into orders (memcode, orddate) values (29, '2022-11-04');
insert into orders (memcode, orddate) values (34, '2021-02-11');
insert into orders (memcode, orddate) values (19, '2023-09-08');
insert into orders (memcode, orddate) values (11, '2022-03-06');
insert into orders (memcode, orddate) values (23, '2024-01-02');
insert into orders (memcode, orddate) values (44, '2021-05-22');
insert into orders (memcode, orddate) values (11, '2022-09-05');
insert into orders (memcode, orddate) values (42, '2022-03-21');
insert into orders (memcode, orddate) values (21, '2022-10-09');
insert into orders (memcode, orddate) values (13, '2023-03-30');
insert into orders (memcode, orddate) values (19, '2024-01-20');
insert into orders (memcode, orddate) values (46, '2024-05-11');
insert into orders (memcode, orddate) values (35, '2021-07-07');
insert into orders (memcode, orddate) values (27, '2021-02-01');
insert into orders (memcode, orddate) values (28, '2024-07-30');
insert into orders (memcode, orddate) values (44, '2022-05-02');
insert into orders (memcode, orddate) values (24, '2021-12-12');
insert into orders (memcode, orddate) values (39, '2022-10-06');
insert into orders (memcode, orddate) values (26, '2022-12-14');
insert into orders (memcode, orddate) values (25, '2021-01-20');
insert into orders (memcode, orddate) values (37, '2023-02-16');
insert into orders (memcode, orddate) values (18, '2023-11-29');
insert into orders (memcode, orddate) values (32, '2022-04-14');
insert into orders (memcode, orddate) values (21, '2024-08-03');
insert into orders (memcode, orddate) values (50, '2024-06-01');
insert into orders (memcode, orddate) values (50, '2021-08-12');
insert into orders (memcode, orddate) values (5, '2023-10-10');
insert into orders (memcode, orddate) values (29, '2023-02-18');
insert into orders (memcode, orddate) values (27, '2022-03-04');
insert into orders (memcode, orddate) values (27, '2022-01-10');
insert into orders (memcode, orddate) values (26, '2021-11-25');
insert into orders (memcode, orddate) values (18, '2022-10-22');
insert into orders (memcode, orddate) values (44, '2023-04-19');
insert into orders (memcode, orddate) values (35, '2024-03-20');
insert into orders (memcode, orddate) values (39, '2023-05-11');
insert into orders (memcode, orddate) values (13, '2023-04-24');
insert into orders (memcode, orddate) values (29, '2021-06-09');
insert into orders (memcode, orddate) values (42, '2022-05-01');
insert into orders (memcode, orddate) values (4, '2022-03-25');
insert into orders (memcode, orddate) values (32, '2023-11-11');
insert into orders (memcode, orddate) values (15, '2023-07-06');
insert into orders (memcode, orddate) values (27, '2022-04-02');
insert into orders (memcode, orddate) values (49, '2022-04-20');
insert into orders (memcode, orddate) values (31, '2023-07-24');
insert into orders (memcode, orddate) values (9, '2021-12-10');
insert into orders (memcode, orddate) values (30, '2022-06-05');
insert into orders (memcode, orddate) values (9, '2022-06-18');
insert into orders (memcode, orddate) values (27, '2024-05-24');
insert into orders (memcode, orddate) values (8, '2023-03-13');
insert into orders (memcode, orddate) values (4, '2021-02-23');
insert into orders (memcode, orddate) values (8, '2022-05-09');
insert into orders (memcode, orddate) values (42, '2024-05-23');
insert into orders (memcode, orddate) values (15, '2024-01-07');
insert into orders (memcode, orddate) values (40, '2024-06-27');
insert into orders (memcode, orddate) values (26, '2024-03-20');
insert into orders (memcode, orddate) values (5, '2023-04-05');
insert into orders (memcode, orddate) values (23, '2021-03-24');
insert into orders (memcode, orddate) values (45, '2023-10-31');
insert into orders (memcode, orddate) values (20, '2023-11-06');
insert into orders (memcode, orddate) values (42, '2022-04-15');
insert into orders (memcode, orddate) values (17, '2021-07-05');
insert into orders (memcode, orddate) values (22, '2023-06-08');
insert into orders (memcode, orddate) values (34, '2023-09-13');
insert into orders (memcode, orddate) values (30, '2022-09-11');
insert into orders (memcode, orddate) values (5, '2024-01-18');
insert into orders (memcode, orddate) values (26, '2021-02-03');
insert into orders (memcode, orddate) values (21, '2021-07-26');
insert into orders (memcode, orddate) values (40, '2022-08-14');
insert into orders (memcode, orddate) values (3, '2021-02-26');
insert into orders (memcode, orddate) values (16, '2023-03-07');
insert into orders (memcode, orddate) values (8, '2022-10-31');
insert into orders (memcode, orddate) values (30, '2022-10-13');
insert into orders (memcode, orddate) values (40, '2021-05-26');
insert into orders (memcode, orddate) values (50, '2023-12-07');
insert into orders (memcode, orddate) values (34, '2023-08-04');
insert into orders (memcode, orddate) values (19, '2021-11-12');
insert into orders (memcode, orddate) values (39, '2023-03-03');
insert into orders (memcode, orddate) values (30, '2022-02-16');
insert into orders (memcode, orddate) values (16, '2021-07-11');
insert into orders (memcode, orddate) values (50, '2023-05-14');
insert into orders (memcode, orddate) values (3, '2021-11-15');
insert into orders (memcode, orddate) values (13, '2023-06-07');
insert into orders (memcode, orddate) values (36, '2024-06-29');
insert into orders (memcode, orddate) values (9, '2023-06-13');
insert into orders (memcode, orddate) values (32, '2022-09-18');
insert into orders (memcode, orddate) values (37, '2024-03-19');
insert into orders (memcode, orddate) values (30, '2021-10-22');
insert into orders (memcode, orddate) values (20, '2023-02-18');
insert into orders (memcode, orddate) values (17, '2022-02-04');
insert into orders (memcode, orddate) values (32, '2021-03-22');
insert into orders (memcode, orddate) values (16, '2024-02-27');
insert into orders (memcode, orddate) values (26, '2023-07-20');
insert into orders (memcode, orddate) values (42, '2021-05-29');
insert into orders (memcode, orddate) values (49, '2021-03-12');
insert into orders (memcode, orddate) values (49, '2021-06-23');
insert into orders (memcode, orddate) values (8, '2024-05-05');
insert into orders (memcode, orddate) values (1, '2022-07-30');
insert into orders (memcode, orddate) values (31, '2021-04-22');
insert into orders (memcode, orddate) values (28, '2022-04-17');
insert into orders (memcode, orddate) values (2, '2022-06-14');
insert into orders (memcode, orddate) values (17, '2024-01-22');
insert into orders (memcode, orddate) values (35, '2023-11-08');
insert into orders (memcode, orddate) values (35, '2024-06-02');
insert into orders (memcode, orddate) values (43, '2023-06-16');
insert into orders (memcode, orddate) values (50, '2021-04-13');
insert into orders (memcode, orddate) values (30, '2023-03-01');
insert into orders (memcode, orddate) values (49, '2022-05-22');
insert into orders (memcode, orddate) values (42, '2024-04-24');
insert into orders (memcode, orddate) values (39, '2023-01-23');
insert into orders (memcode, orddate) values (47, '2023-09-01');
insert into orders (memcode, orddate) values (20, '2021-03-14');
insert into orders (memcode, orddate) values (50, '2022-01-11');
insert into orders (memcode, orddate) values (2, '2023-04-16');
insert into orders (memcode, orddate) values (36, '2022-08-12');
insert into orders (memcode, orddate) values (8, '2021-08-10');
insert into orders (memcode, orddate) values (36, '2023-06-25');
insert into orders (memcode, orddate) values (29, '2023-04-01');
insert into orders (memcode, orddate) values (39, '2023-05-06');
insert into orders (memcode, orddate) values (23, '2021-01-08');
insert into orders (memcode, orddate) values (44, '2021-12-17');
insert into orders (memcode, orddate) values (34, '2022-01-26');
insert into orders (memcode, orddate) values (5, '2024-04-05');
insert into orders (memcode, orddate) values (43, '2021-12-12');
insert into orders (memcode, orddate) values (7, '2023-12-24');
insert into orders (memcode, orddate) values (38, '2024-02-27');
insert into orders (memcode, orddate) values (19, '2023-12-15');
insert into orders (memcode, orddate) values (50, '2023-03-24');
insert into orders (memcode, orddate) values (8, '2024-07-29');
insert into orders (memcode, orddate) values (28, '2022-11-02');
insert into orders (memcode, orddate) values (14, '2022-08-25');
insert into orders (memcode, orddate) values (38, '2021-10-25');
insert into orders (memcode, orddate) values (43, '2022-10-13');
insert into orders (memcode, orddate) values (3, '2023-07-01');
insert into orders (memcode, orddate) values (29, '2024-01-22');
insert into orders (memcode, orddate) values (34, '2023-02-04');
insert into orders (memcode, orddate) values (29, '2023-04-21');
insert into orders (memcode, orddate) values (35, '2021-06-03');
insert into orders (memcode, orddate) values (10, '2024-06-15');
insert into orders (memcode, orddate) values (4, '2024-07-29');
insert into orders (memcode, orddate) values (7, '2021-01-17');
insert into orders (memcode, orddate) values (37, '2024-05-03');
insert into orders (memcode, orddate) values (11, '2022-06-10');
insert into orders (memcode, orddate) values (40, '2021-01-06');
insert into orders (memcode, orddate) values (13, '2021-11-11');
insert into orders (memcode, orddate) values (31, '2021-07-07');
insert into orders (memcode, orddate) values (41, '2021-11-05');
insert into orders (memcode, orddate) values (3, '2024-05-06');
insert into orders (memcode, orddate) values (7, '2022-12-10');
insert into orders (memcode, orddate) values (39, '2023-10-28');
insert into orders (memcode, orddate) values (33, '2024-04-04');
insert into orders (memcode, orddate) values (50, '2022-07-12');
insert into orders (memcode, orddate) values (39, '2024-05-13');
insert into orders (memcode, orddate) values (15, '2022-12-23');
insert into orders (memcode, orddate) values (13, '2021-01-07');
insert into orders (memcode, orddate) values (41, '2021-08-02');
insert into orders (memcode, orddate) values (18, '2022-09-12');
insert into orders (memcode, orddate) values (18, '2024-01-24');
insert into orders (memcode, orddate) values (5, '2021-01-08');
insert into orders (memcode, orddate) values (11, '2023-08-03');
insert into orders (memcode, orddate) values (27, '2022-11-01');
insert into orders (memcode, orddate) values (21, '2021-04-10');
insert into orders (memcode, orddate) values (2, '2023-03-27');
insert into orders (memcode, orddate) values (35, '2021-07-28');
insert into orders (memcode, orddate) values (26, '2024-01-11');
insert into orders (memcode, orddate) values (14, '2023-03-02');
insert into orders (memcode, orddate) values (49, '2021-08-31');
insert into orders (memcode, orddate) values (16, '2024-05-17');
insert into orders (memcode, orddate) values (49, '2024-08-01');

insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (101, 55, 2, 3, 1, 87000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (102, 68, 10, 4, 2, 34000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (103, 144, 7, 1, 3, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (104, 145, 1, 1, 2, 24000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (105, 119, 10, 1, 5, 26000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (106, 77, 9, 1, 3, 104000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (107, 152, 10, 3, 4, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (108, 81, 4, 1, 4, 108000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (109, 138, 10, 1, 5, 90000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (110, 24, 10, 5, 2, 65000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (111, 64, 7, 3, 1, 51000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (112, 25, 8, 4, 3, 47000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (113, 120, 8, 1, 3, 41000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (114, 94, 10, 2, 2, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (115, 77, 10, 1, 2, 32000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (116, 124, 6, 3, 3, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (117, 84, 10, 3, 5, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (118, 35, 4, 4, 1, 75000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (119, 83, 2, 3, 1, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (120, 30, 5, 3, 3, 99000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (121, 114, 9, 1, 1, 58000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (122, 52, 6, 1, 3, 108000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (123, 99, 5, 4, 3, 75000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (124, 66, 8, 3, 1, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (125, 111, 8, 2, 1, 39000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (126, 17, 6, 5, 4, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (127, 141, 2, 1, 1, 108000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (128, 110, 5, 4, 5, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (129, 5, 1, 4, 3, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (130, 54, 5, 2, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (131, 18, 4, 4, 5, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (132, 75, 1, 2, 2, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (133, 90, 3, 1, 2, 89000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (134, 3, 3, 2, 1, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (135, 52, 3, 1, 4, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (136, 86, 3, 1, 4, 93000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (137, 51, 1, 1, 4, 55000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (138, 131, 3, 5, 1, 25000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (139, 74, 3, 5, 2, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (140, 58, 3, 3, 3, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (141, 133, 5, 5, 5, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (142, 118, 1, 4, 3, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (143, 139, 9, 1, 4, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (144, 150, 10, 5, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (145, 117, 10, 5, 1, 101000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (146, 27, 8, 2, 4, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (147, 20, 4, 4, 2, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (148, 143, 5, 1, 1, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (149, 101, 10, 1, 2, 88000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (150, 88, 5, 1, 3, 47000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (151, 47, 2, 5, 1, 101000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (152, 145, 10, 4, 5, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (153, 112, 9, 3, 2, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (154, 71, 7, 2, 3, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (155, 17, 9, 4, 4, 51000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (156, 16, 10, 4, 5, 85000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (157, 152, 4, 2, 4, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (158, 75, 5, 3, 1, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (159, 115, 10, 5, 3, 93000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (160, 28, 3, 3, 1, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (161, 7, 10, 4, 4, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (162, 41, 6, 2, 4, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (163, 113, 2, 2, 2, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (164, 55, 6, 3, 2, 104000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (165, 53, 3, 1, 5, 47000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (166, 106, 3, 4, 3, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (167, 130, 2, 1, 5, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (168, 76, 6, 1, 3, 75000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (169, 98, 4, 4, 1, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (170, 106, 6, 2, 5, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (171, 43, 5, 2, 3, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (172, 9, 10, 2, 1, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (173, 16, 2, 3, 4, 104000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (174, 121, 3, 1, 1, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (175, 50, 6, 5, 2, 103000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (176, 101, 9, 2, 3, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (177, 34, 8, 4, 4, 47000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (178, 76, 4, 4, 4, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (179, 95, 10, 4, 3, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (180, 103, 4, 1, 2, 32000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (181, 104, 7, 1, 2, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (182, 25, 2, 3, 1, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (183, 101, 9, 1, 2, 58000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (184, 9, 3, 1, 5, 65000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (185, 124, 6, 3, 1, 87000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (186, 26, 7, 5, 2, 87000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (187, 54, 9, 3, 3, 104000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (188, 58, 3, 4, 3, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (189, 53, 8, 2, 4, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (190, 25, 7, 2, 5, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (191, 95, 2, 5, 3, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (192, 57, 2, 4, 4, 63000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (193, 5, 6, 2, 1, 34000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (194, 54, 1, 4, 1, 96000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (195, 69, 7, 3, 3, 57000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (196, 100, 1, 4, 5, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (197, 150, 2, 5, 5, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (198, 114, 1, 1, 2, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (199, 65, 3, 3, 5, 107000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (200, 63, 8, 1, 1, 39000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (201, 65, 1, 5, 4, 93000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (202, 140, 3, 3, 4, 96000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (203, 36, 5, 1, 5, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (204, 125, 6, 3, 4, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (205, 141, 1, 4, 5, 108000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (206, 135, 6, 2, 4, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (207, 46, 10, 2, 4, 103000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (208, 92, 4, 3, 1, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (209, 147, 1, 2, 1, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (210, 39, 1, 5, 1, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (211, 127, 6, 1, 2, 35000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (212, 140, 9, 4, 2, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (213, 152, 1, 4, 1, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (214, 102, 2, 4, 4, 89000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (215, 22, 1, 3, 2, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (216, 141, 5, 4, 5, 82000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (217, 109, 2, 2, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (218, 21, 8, 5, 3, 31000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (219, 100, 7, 4, 1, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (220, 2, 7, 4, 3, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (221, 26, 10, 3, 2, 102000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (222, 135, 3, 5, 2, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (223, 38, 4, 3, 5, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (224, 39, 7, 4, 1, 96000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (225, 73, 3, 1, 1, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (226, 26, 1, 5, 5, 36000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (227, 51, 2, 1, 1, 101000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (228, 57, 8, 4, 1, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (229, 100, 2, 1, 5, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (230, 44, 9, 3, 5, 77000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (231, 126, 5, 4, 3, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (232, 151, 9, 1, 5, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (233, 86, 3, 3, 5, 48000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (234, 45, 6, 4, 3, 31000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (235, 97, 2, 1, 3, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (236, 125, 10, 1, 4, 90000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (237, 113, 9, 5, 2, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (238, 112, 7, 5, 4, 55000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (239, 81, 9, 3, 4, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (240, 87, 10, 2, 1, 29000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (241, 148, 6, 1, 4, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (242, 124, 10, 3, 1, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (243, 31, 3, 5, 1, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (244, 117, 3, 1, 2, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (245, 8, 6, 1, 3, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (246, 79, 10, 5, 2, 36000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (247, 53, 5, 4, 3, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (248, 109, 8, 5, 1, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (249, 86, 4, 4, 5, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (250, 107, 9, 5, 3, 27000);

insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (163, 6, 6, 3, 2, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (37, 33, 3, 1, 4, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (112, 21, 5, 3, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (40, 111, 2, 5, 2, 97000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (101, 20, 1, 5, 5, 43000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (185, 131, 10, 3, 1, 78000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (63, 84, 8, 5, 5, 43000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (27, 107, 1, 1, 4, 77000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (202, 70, 2, 4, 5, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (106, 116, 1, 1, 2, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (66, 9, 3, 2, 5, 92000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (6, 65, 2, 3, 4, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (160, 12, 7, 4, 5, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (130, 46, 6, 4, 1, 107000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (90, 69, 7, 4, 3, 30000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (210, 141, 7, 2, 4, 63000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 148, 6, 4, 1, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (247, 22, 8, 3, 5, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (244, 43, 1, 1, 2, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (209, 102, 10, 3, 1, 87000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (195, 91, 7, 5, 1, 101000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (33, 129, 9, 4, 5, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (14, 101, 8, 1, 4, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (182, 147, 4, 1, 5, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (59, 99, 3, 1, 2, 108000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (100, 19, 5, 2, 4, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (43, 54, 10, 1, 5, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (220, 7, 4, 5, 5, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (194, 20, 5, 3, 2, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 43, 9, 2, 4, 107000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (154, 135, 7, 5, 4, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (61, 86, 10, 3, 2, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (224, 118, 9, 2, 4, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (52, 62, 9, 3, 5, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (219, 89, 2, 1, 3, 58000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (95, 10, 6, 3, 1, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (57, 12, 5, 1, 3, 59000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (24, 24, 1, 1, 5, 36000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (175, 117, 10, 5, 4, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (27, 151, 2, 2, 4, 48000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (34, 93, 9, 1, 4, 105000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (73, 43, 10, 4, 2, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (89, 148, 7, 2, 2, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (120, 20, 7, 4, 1, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (10, 72, 1, 2, 4, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (232, 137, 3, 3, 5, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (121, 111, 5, 4, 4, 89000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (85, 48, 1, 2, 3, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (242, 6, 9, 3, 3, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (231, 64, 6, 4, 5, 65000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (102, 26, 5, 5, 5, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (68, 55, 8, 5, 2, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (97, 40, 5, 4, 5, 52000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (141, 116, 9, 1, 5, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (182, 23, 5, 3, 1, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (148, 139, 6, 1, 2, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (93, 129, 6, 2, 3, 35000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (82, 30, 5, 4, 4, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (137, 21, 8, 5, 1, 59000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (101, 85, 1, 4, 4, 59000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (95, 87, 8, 1, 2, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (121, 56, 1, 2, 1, 46000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (203, 121, 8, 1, 5, 75000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (222, 106, 5, 4, 2, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (53, 37, 6, 5, 2, 89000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (132, 147, 7, 5, 2, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (164, 151, 10, 3, 5, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 11, 6, 5, 1, 23000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (167, 42, 10, 1, 2, 92000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (81, 105, 4, 5, 1, 44000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (100, 98, 3, 3, 1, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (179, 5, 7, 3, 5, 90000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (12, 83, 7, 2, 5, 27000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (31, 65, 6, 4, 4, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 84, 7, 1, 4, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (181, 29, 5, 3, 2, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (189, 19, 5, 4, 3, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (21, 58, 6, 4, 3, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (190, 32, 6, 4, 5, 93000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (222, 134, 2, 3, 4, 27000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (32, 5, 9, 3, 4, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (72, 94, 6, 5, 3, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (42, 5, 6, 5, 1, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (234, 101, 6, 2, 2, 99000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (140, 94, 5, 1, 2, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (106, 134, 4, 2, 1, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (174, 7, 6, 2, 3, 32000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (113, 139, 8, 2, 4, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 61, 7, 4, 4, 89000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (144, 147, 6, 1, 5, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (150, 27, 9, 4, 4, 102000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (239, 76, 10, 5, 3, 85000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (75, 45, 7, 4, 2, 64000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (62, 70, 8, 5, 3, 101000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (67, 156, 8, 3, 2, 104000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (59, 82, 2, 3, 4, 59000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (99, 144, 6, 1, 1, 29000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (147, 86, 2, 4, 3, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (20, 78, 6, 3, 4, 46000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (166, 57, 2, 4, 4, 104000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (95, 51, 7, 2, 3, 58000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 104, 4, 2, 4, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (107, 110, 2, 2, 2, 44000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 47, 3, 3, 3, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 34, 4, 5, 5, 41000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (191, 133, 6, 3, 2, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (141, 145, 1, 1, 5, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (46, 73, 6, 1, 4, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (33, 130, 2, 1, 3, 51000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (191, 63, 4, 4, 1, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (209, 81, 3, 4, 1, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 18, 6, 3, 2, 88000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (197, 115, 7, 4, 4, 44000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (206, 144, 2, 1, 4, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (139, 8, 7, 5, 2, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (79, 21, 9, 5, 1, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (102, 108, 7, 3, 4, 63000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (158, 61, 5, 3, 4, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (104, 121, 4, 5, 2, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (42, 48, 10, 5, 3, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (142, 154, 10, 3, 3, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (76, 60, 9, 5, 1, 57000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (223, 87, 2, 4, 3, 102000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (38, 152, 10, 5, 4, 78000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (4, 30, 4, 3, 4, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (175, 14, 3, 1, 2, 100000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (138, 91, 8, 2, 1, 59000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (14, 69, 2, 1, 4, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (94, 41, 3, 5, 4, 56000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (171, 108, 7, 4, 2, 98000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (190, 16, 6, 4, 1, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (235, 3, 1, 5, 3, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (136, 128, 8, 5, 4, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (68, 91, 1, 5, 4, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (116, 81, 9, 4, 1, 96000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (204, 119, 2, 1, 1, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (141, 78, 8, 4, 5, 43000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (8, 59, 2, 4, 5, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (120, 50, 3, 2, 3, 40000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (126, 54, 5, 5, 5, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (89, 113, 7, 5, 5, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (178, 54, 8, 3, 5, 29000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (115, 26, 3, 3, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (191, 108, 6, 4, 1, 27000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (15, 15, 1, 3, 1, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (184, 106, 9, 3, 4, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (15, 124, 8, 5, 1, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (9, 142, 1, 3, 1, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (175, 114, 2, 1, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (105, 84, 3, 4, 5, 57000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (155, 146, 7, 5, 2, 103000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (23, 29, 8, 3, 2, 31000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (246, 49, 5, 4, 5, 29000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (168, 55, 8, 5, 5, 47000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (214, 46, 6, 3, 3, 93000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (136, 62, 8, 1, 5, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (198, 6, 2, 1, 1, 85000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (144, 70, 7, 4, 5, 101000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (192, 75, 2, 1, 5, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (241, 17, 5, 3, 5, 107000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (67, 120, 9, 3, 3, 107000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (237, 63, 8, 4, 3, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (134, 132, 7, 3, 5, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (144, 44, 7, 5, 4, 101000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (111, 75, 1, 4, 4, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (62, 66, 10, 2, 1, 27000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (92, 127, 3, 1, 4, 90000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (205, 78, 5, 4, 1, 99000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (191, 128, 1, 2, 3, 57000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (188, 38, 1, 3, 5, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (213, 130, 7, 2, 1, 97000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (178, 88, 8, 2, 5, 90000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (76, 24, 9, 3, 5, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (146, 151, 10, 4, 4, 75000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (97, 83, 9, 4, 3, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (150, 42, 7, 1, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (61, 105, 5, 4, 1, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (208, 119, 1, 3, 4, 78000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (238, 133, 2, 3, 1, 51000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (69, 81, 8, 1, 5, 31000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (45, 88, 7, 3, 3, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (119, 117, 1, 3, 1, 52000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (43, 149, 3, 1, 4, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (169, 81, 2, 5, 2, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (188, 96, 6, 4, 5, 32000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (199, 116, 8, 2, 3, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (85, 56, 3, 3, 5, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (112, 70, 5, 2, 5, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (73, 112, 1, 1, 5, 25000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (84, 21, 4, 1, 3, 103000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (122, 15, 4, 5, 1, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (70, 17, 6, 3, 4, 87000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (29, 13, 7, 2, 3, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (51, 56, 4, 5, 1, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (246, 109, 10, 3, 5, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (235, 132, 10, 1, 2, 24000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (102, 49, 2, 2, 5, 24000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (187, 29, 2, 3, 2, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (248, 71, 1, 5, 4, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (189, 21, 3, 4, 3, 74000);

insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (62, 197, 6, 5, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (149, 180, 3, 4, 3, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (41, 262, 9, 4, 4, 48000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (54, 327, 8, 4, 4, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (9, 428, 6, 4, 3, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (240, 369, 3, 1, 2, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (142, 200, 1, 2, 4, 29000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (147, 214, 2, 3, 5, 65000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (5, 243, 4, 4, 1, 78000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (229, 202, 5, 2, 5, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (214, 350, 9, 5, 4, 53000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (212, 239, 8, 1, 2, 74000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (104, 379, 7, 4, 5, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (243, 211, 9, 5, 2, 100000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (232, 319, 5, 4, 2, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (57, 176, 4, 5, 3, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (244, 167, 3, 1, 3, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (113, 214, 9, 3, 4, 58000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (179, 377, 5, 2, 4, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (117, 353, 10, 5, 5, 64000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (241, 249, 3, 1, 2, 55000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (61, 386, 7, 4, 1, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (127, 200, 10, 5, 3, 39000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (173, 391, 7, 5, 2, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (7, 186, 4, 5, 1, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (178, 244, 2, 1, 1, 26000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (6, 221, 9, 1, 3, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (213, 383, 2, 4, 3, 63000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (82, 201, 7, 3, 3, 27000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 207, 4, 1, 5, 28000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (74, 313, 1, 3, 5, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (223, 330, 6, 2, 1, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (182, 303, 3, 5, 3, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (131, 159, 8, 4, 5, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (18, 445, 9, 2, 4, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (129, 286, 7, 1, 1, 75000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (16, 289, 1, 3, 1, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (53, 253, 7, 1, 2, 58000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (145, 301, 5, 1, 1, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (195, 220, 10, 3, 3, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (12, 442, 3, 1, 3, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (167, 180, 3, 5, 3, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (193, 188, 10, 3, 2, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (115, 307, 8, 5, 1, 57000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (153, 171, 9, 5, 3, 6000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (118, 394, 6, 4, 5, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (32, 216, 4, 2, 3, 36000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (201, 168, 8, 3, 4, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (140, 211, 1, 5, 2, 52000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (82, 363, 6, 4, 2, 23000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (233, 410, 5, 4, 4, 24000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (179, 262, 2, 4, 4, 9000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (193, 372, 3, 5, 4, 97000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 295, 3, 4, 1, 56000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (90, 168, 10, 5, 1, 75000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (81, 420, 10, 4, 4, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (167, 363, 7, 3, 5, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (212, 300, 5, 1, 2, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (201, 241, 6, 3, 3, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (77, 323, 4, 4, 1, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (150, 265, 6, 5, 3, 88000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (105, 192, 5, 1, 5, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (93, 367, 5, 2, 5, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (185, 275, 8, 4, 4, 98000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (11, 394, 10, 2, 4, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 396, 2, 4, 4, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (41, 446, 2, 3, 4, 97000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (24, 252, 2, 4, 1, 90000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (134, 339, 5, 4, 1, 53000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (197, 376, 10, 1, 3, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (54, 203, 6, 5, 3, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (208, 303, 2, 4, 3, 90000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 302, 2, 1, 2, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (239, 251, 2, 2, 1, 23000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (20, 370, 2, 3, 4, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (74, 350, 8, 4, 2, 82000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (6, 267, 2, 4, 4, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (249, 346, 4, 2, 4, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (156, 177, 6, 2, 5, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (206, 195, 3, 5, 4, 7000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (29, 190, 8, 5, 4, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (123, 174, 4, 1, 2, 98000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (103, 194, 7, 5, 2, 55000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (224, 187, 1, 5, 1, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (134, 265, 4, 1, 2, 53000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (70, 263, 9, 3, 2, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (184, 180, 3, 1, 4, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (243, 311, 1, 1, 5, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (189, 371, 7, 2, 4, 82000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (163, 262, 3, 4, 4, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (169, 162, 2, 1, 4, 87000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (251, 163, 1, 5, 4, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (65, 356, 8, 2, 1, 73000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (158, 256, 8, 1, 5, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (222, 283, 8, 4, 2, 63000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (120, 189, 6, 1, 3, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (122, 405, 9, 3, 4, 34000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (238, 214, 7, 4, 4, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (139, 322, 4, 5, 3, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (92, 290, 5, 1, 4, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (75, 220, 5, 4, 3, 77000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (125, 367, 9, 5, 3, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (227, 320, 3, 5, 3, 69000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (59, 178, 2, 2, 3, 58000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (231, 257, 7, 3, 3, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (232, 192, 6, 4, 4, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 185, 2, 4, 1, 38000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (154, 288, 6, 5, 5, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (138, 338, 5, 4, 5, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (126, 353, 7, 4, 2, 92000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (179, 230, 2, 5, 5, 46000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (163, 178, 4, 4, 3, 59000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (84, 209, 7, 3, 3, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (34, 373, 7, 1, 5, 99000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (251, 229, 5, 2, 3, 3000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (240, 208, 4, 1, 5, 54000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (186, 282, 10, 5, 1, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (241, 371, 3, 4, 5, 23000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (238, 431, 3, 1, 1, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (40, 219, 1, 2, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (115, 334, 3, 4, 4, 36000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (217, 288, 3, 1, 4, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (125, 285, 3, 4, 5, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (128, 165, 5, 5, 2, 25000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (229, 179, 1, 1, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (222, 339, 4, 2, 5, 83000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (86, 286, 8, 2, 1, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (205, 334, 3, 3, 3, 8000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (49, 231, 9, 5, 1, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (46, 301, 3, 1, 4, 26000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (229, 156, 8, 5, 1, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (110, 376, 1, 1, 2, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (173, 435, 8, 1, 5, 9000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (81, 345, 9, 4, 4, 53000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (76, 278, 4, 4, 2, 26000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (113, 288, 1, 3, 4, 51000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (65, 342, 3, 1, 5, 33000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (227, 405, 9, 4, 4, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (142, 299, 5, 2, 3, 100000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (129, 188, 1, 2, 2, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (168, 319, 5, 1, 5, 39000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (229, 409, 6, 2, 1, 28000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (197, 228, 3, 2, 1, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (120, 180, 6, 1, 3, 31000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (9, 282, 1, 3, 1, 85000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (111, 391, 1, 5, 1, 3000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (192, 414, 5, 1, 5, 36000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (190, 305, 4, 5, 4, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (94, 411, 5, 5, 2, 3000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (152, 359, 3, 5, 2, 100000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 308, 9, 5, 2, 80000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (150, 167, 9, 2, 4, 84000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (242, 214, 1, 3, 4, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (202, 393, 9, 2, 2, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (2, 293, 7, 2, 5, 70000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (248, 228, 2, 2, 4, 71000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 265, 7, 4, 2, 61000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (186, 394, 7, 2, 5, 48000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (124, 162, 9, 4, 4, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (240, 218, 2, 3, 5, 68000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (55, 326, 9, 5, 2, 2000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (116, 158, 2, 1, 2, 27000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (245, 202, 4, 5, 1, 76000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (238, 328, 7, 2, 2, 97000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (63, 363, 8, 4, 5, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (158, 236, 1, 3, 2, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (66, 245, 7, 3, 3, 56000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (53, 293, 5, 1, 2, 93000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (168, 324, 5, 1, 5, 85000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 361, 4, 2, 2, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (227, 394, 9, 4, 2, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (183, 330, 9, 1, 2, 42000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (42, 353, 2, 2, 1, 79000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (5, 226, 1, 2, 1, 94000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (26, 259, 7, 4, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (137, 377, 10, 4, 4, 47000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (159, 244, 9, 4, 4, 94000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (214, 357, 7, 4, 3, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (85, 365, 1, 5, 1, 92000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (75, 324, 8, 2, 4, 78000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (95, 250, 6, 1, 2, 98000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (186, 248, 7, 5, 4, 37000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (253, 316, 7, 4, 4, 27000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (168, 265, 4, 1, 5, 62000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (242, 280, 6, 4, 3, 95000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (178, 173, 5, 2, 4, 86000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (35, 412, 4, 1, 3, 50000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (95, 300, 7, 1, 2, 72000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (192, 331, 10, 5, 4, 81000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (255, 175, 2, 1, 1, 55000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (225, 216, 10, 3, 1, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (255, 278, 9, 4, 2, 47000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (94, 440, 3, 4, 1, 9000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (206, 301, 8, 5, 2, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (32, 185, 1, 1, 4, 84000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (6, 394, 10, 1, 2, 45000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (224, 448, 5, 4, 4, 60000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (98, 216, 10, 4, 5, 49000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (30, 367, 10, 2, 1, 91000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (174, 332, 10, 1, 2, 67000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 33, 10, 1, 2, 67000);