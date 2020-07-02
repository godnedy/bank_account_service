create table if not exists USER
(
  ID  nvarchar(36) not null,
  PERSONAL_ID_NUMBER char(11) not null,
  FULL_NAME varchar(200) not null,
  primary key (ID)
);

create table if not exists  SUB_ACCOUNT
(
  ID nvarchar(36) not null,
  CURRENCY VARCHAR(3)   not null,
  USER_ID  nvarchar(36) not null,
  BALANCE  double       not null,
  primary key (ID),
  constraint SUB_ACCOUNT_USER_FK FOREIGN KEY (USER_ID) references USER (ID));

