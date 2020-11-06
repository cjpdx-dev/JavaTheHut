USE "234a_JavaTheHut";

CREATE TABLE USERS
     (   UserID          INT        NOT NULL	IDENTITY(1001, 1),      
         UserName        NVARCHAR(25)    NOT NULL,
         Password        NVARCHAR(30)    NOT NULL,
		 Email           NVARCHAR(30)    NOT NULL,
		 Role            NVARCHAR(10)    NOT NULL,
		 CONSTRAINT      USERS_pk PRIMARY KEY (UserID)
      );
--PAUSE

CREATE TABLE TEST
      (  TestID          INT       NOT NULL,
	     TestName        NVARCHAR(10)   NOT NULL,
	     CONSTRAINT      TEST_pk PRIMARY KEY(TestID)
	  );
--PAUSE


CREATE TABLE ITEM
      (  ItemID          INT      NOT NULL,
	     TestID          INT      NOT NULL,
		 ItemName        NVARCHAR(15)  NOT NULL,
		 CONSTRAINT      ITEM_pk PRIMARY KEY(ItemID)
	   );
--PAUSE

CREATE TABLE SESSION 
       ( SessionID       INT      NOT NULL,
	     UserID          INT      NOT NULL,
		 TestID          INT      NOT NULL,
		 SessionDate     DATETIME      NOT NULL DEFAULT GETDATE(),
		 CONSTRAINT      SESSION_pk PRIMARY KEY(SessionID),
		 CONSTRAINT      USERS_fk FOREIGN KEY(UserID) REFERENCES  USERS,
		 CONSTRAINT      TEST_fk FOREIGN KEY(TestID)  REFERENCES  TEST
		 );
--PAUSE

CREATE TABLE RESULT 
       ( ResultID        INT      NOT NULL,
	     SessionID       INT      NOT NULL,
		 Item1ID         INT      NOT NULL  FOREIGN KEY REFERENCES ITEM(ItemID),
		 Item2ID         INT      NOT NULL  FOREIGN KEY REFERENCES ITEM(ItemID),
		 ResultCode      INT      NOT NULL
		 CONSTRAINT      RESULT_PK     PRIMARY KEY (ResultID), 
		 CONSTRAINT      FK_SESSION      FOREIGN KEY (SessionID) REFERENCES SESSION);
		 
--PAUSE
