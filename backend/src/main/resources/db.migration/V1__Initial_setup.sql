CREATE TABLE "member" (
    "memberId" BIGSERIAL NOT NULL PRIMARY KEY,
    "firstName" VARCHAR(50) NOT NULL,
    "lastName" VARCHAR(50) NOT NULL,
    "username" VARCHAR(50) NOT NULL,
    "password" VARCHAR(50) NOT NULL,
    "email" VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE "meeting" (
    "meetingId" BIGSERIAL NOT NULL PRIMARY KEY UNIQUE,
    "meetingTopic" VARCHAR(50) NOT NULL,
    "meetingName" VARCHAR(50) NOT NULL,
    "meetingDate" DATE NOT NULL,
    "meetingDuration" VARCHAR(50) NOT NULL,
    "meetingDescription" TEXT NOT NULL
);

CREATE TABLE "attendeeTable" (
    "attendeeId" BIGSERIAL NOT NULL PRIMARY KEY UNIQUE,
    "memberId" BIGINT NOT NULL,
    "meetingId" BIGINT NOT NULL,
    FOREIGN KEY ("memberId") REFERENCES member("memberId"),
    FOREIGN KEY ("meetingId") REFERENCES meeting("meetingId")
);
