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
insert into product(prodname, prodprice, prodgender, proddesc) values ("반팔티1화이트", 10000, 'M', '티셔츠설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("반팔티1블랙", 10000, 'M', '반팔설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("양말3", 15000, 'U', '양말설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("모자1", 20000, 'F', '모자설명');
insert into product(prodname, prodprice, prodgender, proddesc) values ("청바지1", 22000, 'F', '청바지설명');

# productdetail
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (1, 1, 1, 'S', "tshirt1white.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (1, 1, 2, 'M', "tshirt1black.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (1, 4, 2, 'L', "socks3.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (2, 3, 3, 'M', "cap1.png", "2022-08-01");
insert into productdetail(prodcode, prodcatecode, colorcode, prodsize, prodfilename, proddate) values (2, 2, 4, 'XXL', "jeans1.png", "2022-08-01");

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

# reply
insert into reply(supcode, replycontent, replydate) values(1, '답글1', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(2, '답글2', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(3, '답글3', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(4, '답글4', '2024-07-31');
insert into reply(supcode, replycontent, replydate) values(5, '답글5', '2024-07-31');

# admin
insert into admin(adminid, adminpw) values ('qwe123', 'qwe123');
insert into admin(adminid, adminpw) values ('asd456', 'asd456');
insert into admin(adminid, adminpw) values ('zxc789', 'zxc789');
insert into admin(adminid, adminpw) values ('rty012', 'rty012');
insert into admin(adminid, adminpw) values ('fgh345', 'fgh345');