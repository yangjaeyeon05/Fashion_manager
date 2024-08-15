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
    mememail varchar(30) not null unique,	# 회원이메일
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
    foreign key (proddetailcode) references productdetail(proddetailcode),
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
    foreign key (proddetailcode) references productdetail(proddetailcode)
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
insert into product(prodname, prodprice, prodgender, proddesc) values ("반팔티2", 10000, 'M', '반팔설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("양말3", 15000, 'U', '양말설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("모자1", 20000, 'F', '모자설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("청바지1", 22000, 'F', '청바지설명');

# productdetail
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (1, 1, 1, 'S', "tshirt1white.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (2, 1, 2, 'M', "tshirt1black.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (3, 4, 2, 'L', "socks3.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (4, 3, 3, 'M', "cap1.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (5, 2, 4, 'XXL', "jeans1.png", "2022-08-01");

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
select * from support order by supcode desc;

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
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (8, 5, 6, 3, 2, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 5, 6, 3, 2, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (16, 4, 1, 4, 4, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (78, 5, 8, 3, 1, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (22, 5, 1, 1, 3, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (96, 4, 9, 2, 4, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (10, 1, 4, 1, 4, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (34, 3, 2, 2, 4, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (18, 4, 4, 5, 4, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (45, 2, 2, 2, 2, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (89, 1, 9, 5, 3, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (6, 2, 10, 4, 1, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 1, 8, 5, 1, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (33, 1, 1, 2, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (67, 3, 7, 1, 3, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (86, 3, 7, 5, 1, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 5, 7, 5, 5, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (24, 3, 10, 3, 1, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 1, 1, 3, 4, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (27, 2, 1, 3, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 5, 1, 1, 2, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (5, 3, 6, 5, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 4, 1, 4, 3, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (69, 3, 4, 1, 3, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 3, 5, 4, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (77, 2, 2, 2, 3, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 3, 9, 4, 2, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (84, 2, 7, 1, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (29, 2, 3, 3, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 2, 1, 2, 5, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (60, 2, 5, 5, 2, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (36, 3, 1, 5, 3, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (74, 2, 6, 5, 1, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (97, 3, 8, 4, 1, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (62, 1, 7, 1, 5, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (55, 1, 2, 4, 5, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 4, 1, 5, 5, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (21, 3, 4, 3, 2, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (5, 3, 8, 4, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (4, 1, 5, 1, 3, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (93, 1, 5, 4, 1, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (2, 3, 8, 3, 1, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (19, 3, 10, 1, 5, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (26, 2, 9, 3, 3, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (68, 3, 2, 3, 4, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (70, 5, 3, 5, 2, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (39, 1, 10, 5, 4, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (34, 1, 4, 1, 2, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 2, 9, 5, 5, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (89, 5, 6, 1, 1, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (69, 4, 10, 1, 1, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (96, 1, 4, 3, 3, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (39, 1, 5, 2, 3, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (45, 2, 5, 5, 5, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (35, 4, 10, 1, 4, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (70, 5, 2, 2, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (54, 2, 5, 4, 4, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 1, 10, 5, 3, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 4, 2, 4, 5, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (10, 4, 9, 5, 4, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (56, 2, 6, 4, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (91, 5, 10, 5, 2, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (56, 5, 10, 4, 4, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (78, 5, 7, 2, 2, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (2, 3, 4, 3, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (13, 3, 9, 3, 5, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 5, 5, 3, 4, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (69, 3, 6, 3, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 1, 5, 4, 3, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (62, 1, 4, 1, 3, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (4, 5, 7, 5, 1, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (67, 4, 2, 4, 3, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (41, 5, 5, 4, 5, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (58, 5, 2, 2, 5, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (66, 3, 9, 3, 2, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (4, 5, 3, 2, 3, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (16, 2, 5, 2, 2, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (70, 2, 2, 1, 5, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (31, 1, 4, 5, 1, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (5, 5, 10, 4, 2, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (9, 1, 6, 5, 3, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (81, 1, 4, 2, 1, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (82, 2, 9, 1, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (94, 3, 1, 5, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (5, 5, 3, 4, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (65, 4, 5, 5, 3, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 5, 1, 5, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (7, 2, 3, 2, 4, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (65, 3, 4, 4, 3, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (46, 3, 10, 3, 4, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 2, 8, 3, 4, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 3, 6, 2, 4, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 2, 5, 4, 1, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (70, 1, 2, 5, 2, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (51, 2, 2, 4, 2, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (85, 3, 6, 1, 1, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (94, 4, 1, 5, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (76, 5, 7, 2, 4, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (41, 4, 3, 5, 5, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (13, 1, 4, 3, 3, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (63, 5, 9, 3, 3, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (48, 3, 4, 3, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (24, 4, 6, 5, 4, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (56, 5, 8, 3, 1, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (85, 3, 7, 2, 4, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (28, 2, 10, 4, 4, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (56, 5, 9, 4, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (98, 4, 9, 2, 2, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (16, 5, 3, 3, 2, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (28, 5, 4, 3, 2, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 5, 5, 5, 3, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (46, 3, 8, 5, 4, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (63, 1, 10, 1, 4, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (21, 4, 3, 2, 2, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (12, 1, 8, 2, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 5, 5, 4, 4, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (77, 3, 5, 1, 1, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (55, 2, 5, 3, 4, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (14, 1, 8, 1, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (57, 1, 3, 4, 2, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (67, 3, 7, 1, 1, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (97, 2, 10, 5, 3, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (36, 4, 8, 4, 5, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (18, 3, 9, 2, 5, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (48, 1, 5, 5, 1, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (91, 1, 6, 5, 4, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (72, 4, 6, 2, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (43, 5, 1, 4, 4, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (49, 5, 7, 5, 1, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 5, 4, 5, 2, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (57, 4, 1, 4, 3, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (46, 3, 4, 5, 3, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (63, 2, 4, 1, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (86, 5, 4, 1, 2, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (86, 2, 6, 4, 1, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (7, 4, 8, 4, 2, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (35, 1, 5, 2, 4, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 4, 1, 5, 3, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (3, 3, 8, 4, 5, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (84, 5, 5, 2, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (2, 3, 7, 4, 1, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (96, 5, 3, 2, 2, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (42, 1, 8, 4, 1, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (12, 5, 1, 5, 1, 12000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (99, 3, 10, 4, 1, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (19, 3, 9, 2, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (16, 1, 8, 5, 5, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (97, 3, 10, 3, 1, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (29, 3, 2, 3, 3, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (9, 3, 3, 5, 3, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (86, 5, 7, 4, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (62, 5, 3, 2, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (94, 3, 4, 5, 3, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (47, 5, 6, 3, 3, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (88, 3, 6, 2, 4, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (12, 1, 3, 3, 2, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (45, 4, 4, 4, 3, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (79, 5, 5, 4, 5, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (68, 2, 7, 1, 3, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (39, 3, 6, 1, 5, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 2, 6, 1, 5, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (49, 1, 10, 2, 4, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (83, 5, 4, 1, 4, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (63, 5, 1, 5, 5, 13000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (33, 4, 6, 5, 2, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (6, 4, 10, 3, 1, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (40, 4, 6, 3, 3, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (1, 2, 4, 4, 3, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (92, 4, 3, 3, 4, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (97, 4, 4, 1, 2, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (25, 5, 4, 2, 5, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (20, 4, 4, 4, 5, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (99, 1, 2, 2, 5, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (71, 2, 5, 3, 3, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 5, 5, 3, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (39, 5, 6, 2, 2, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 3, 7, 4, 2, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (34, 2, 6, 2, 4, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (80, 4, 10, 3, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (40, 2, 4, 3, 4, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (9, 3, 5, 1, 3, 11000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (7, 3, 10, 4, 2, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (87, 5, 4, 2, 2, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 2, 6, 1, 1, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (47, 4, 2, 4, 2, 18000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (90, 4, 9, 5, 3, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (30, 4, 9, 3, 4, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (92, 1, 4, 5, 5, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (93, 1, 6, 2, 5, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (10, 4, 5, 4, 1, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (19, 5, 4, 1, 5, 10000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (43, 2, 1, 4, 3, 16000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (15, 3, 2, 5, 2, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (11, 3, 2, 3, 5, 19000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (17, 4, 9, 4, 4, 15000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (22, 2, 4, 4, 2, 14000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (61, 2, 5, 5, 5, 21000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (50, 3, 8, 3, 2, 22000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (86, 1, 3, 5, 5, 20000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (77, 2, 10, 5, 5, 17000);
insert into orderdetail (ordcode, proddetailcode, ordamount, ordstate, coupcode, ordprice) values (44, 4, 1, 2, 2, 10000);

select * from support;
select * from support inner join members on support.memcode = members.memcode where supcode = 9;

use fashionmanager;
select * from product;
select * from productdetail;
select * from productdetail where proddate between '2022-01-01' and '2022-01-03';
select *
from productdetail a inner join product b on a.prodcode=b.prodcode inner join productcategory c on a.prodcatecode=c.prodcatecode inner join color d
on a.colorcode=d.colorcode where a.proddate between '"2022-01-01"' and '"2022-01-03"'



