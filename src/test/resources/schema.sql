create table if not exists USER (
  ID                       nvarchar(36) not null, -- not sure if needed
  PERSONAL_IDENTITY_NUMBER char(11)     not null,
  FULL_NAME                varchar(200) not null,
  primary key (ID),
  constraint unique_personal_identity_number UNIQUE KEY (PERSONAL_IDENTITY_NUMBER)
);

create table if not exists SUB_ACCOUNT (
  ID nvarchar(36) not null, -- not sure if needed
  CURRENCY VARCHAR(3) not null,
  USER_ID nvarchar(36) not null,
  BALANCE double not null,
  primary key (ID),
  constraint SUB_ACCOUNT_USER_FK FOREIGN KEY (USER_ID) references USER (ID),
  constraint SUB_ACCOUNT_UNIQUE_USER_ID_CURRENCY unique (USER_ID, CURRENCY)
);

create index USER_PERSONAL_IDENTITY_NUMBER_IDX on USER(PERSONAL_IDENTITY_NUMBER);

create index SUB_ACCOUNT_USER_ID_IDX on SUB_ACCOUNT(USER_ID);
