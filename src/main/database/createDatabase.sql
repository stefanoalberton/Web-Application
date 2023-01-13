-- USER FOR HYPERU_DB

-- name: hyperu
-- password: admin

-- Database creations
CREATE DATABASE HyperU_DB OWNER hyperu ENCODING='UTF8';

-- Connect to HyperU_DB db to create data for its 'public' schema
\c HyperU_DB

-- Create new data types
CREATE TYPE gendertype AS ENUM (
    'Not Declared',
    'Male',
    'Female'
);

CREATE TYPE usertype AS ENUM (
    'Common User',
    'Moderator',
    'Administrator'
);

CREATE TYPE statustype AS ENUM (
    'Pending',
    'Accepted'
);

-- Create username domain
CREATE DOMAIN username AS VARCHAR
    CHECK (((VALUE)::text ~ '^[a-zA-Z0-9._-]*$'));

-- Create email domain
CREATE DOMAIN email AS VARCHAR
    CHECK (((VALUE)::text ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'));

-- Table Creation

-- Password has CHAR(32) because hash function return a string of 32 characters
-- Users
CREATE TABLE Users (
    ID SERIAL,
    Username USERNAME NOT NULL,
    Email EMAIL NOT NULL,
    Password CHAR(32),
    Role USERTYPE NOT NULL DEFAULT 'Common User',
    Banned BOOLEAN NOT NULL DEFAULT FALSE,

    PRIMARY KEY (ID),
    UNIQUE (Username)
);

-- Profile
CREATE TABLE Profile (
    ID INTEGER,
    Name VARCHAR NOT NULL,
    Surname VARCHAR NOT NULL,
    BirthDate DATE DEFAULT NULL,
    Gender GENDERTYPE DEFAULT 'Not Declared',
    Profile_Picture BYTEA DEFAULT NULL,
    Biography VARCHAR(700) DEFAULT '',

    PRIMARY KEY (ID),
    FOREIGN KEY (ID) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Skill
CREATE TABLE Skill (
    ID SERIAL,
    Name VARCHAR NOT NULL,
    Description VARCHAR NOT NULL,

    PRIMARY KEY (ID)
);

-- Topic
CREATE TABLE Topic (
    ID SERIAL,
    Name VARCHAR NOT NULL,
    Description VARCHAR NOT NULL,

    PRIMARY KEY (ID)
);

-- Idea
CREATE TABLE Idea (
    ID SERIAL,
    Title VARCHAR NOT NULL,
    Description VARCHAR NOT NULL,
    Image BYTEA DEFAULT NULL,
    Posted_Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Num_Likes INTEGER CHECK (Num_Likes >= 0) DEFAULT 0,
    ID_Creator INTEGER,

    PRIMARY KEY (ID),
    FOREIGN KEY (ID_Creator) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Team
CREATE TABLE Team (
    ID SERIAL,
    Name VARCHAR NOT NULL,
    Creation_Time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ID_Idea INTEGER,
    Accept_Requests BOOLEAN NOT NULL DEFAULT True,

    PRIMARY KEY (ID),
    FOREIGN KEY (ID_Idea) REFERENCES Idea(ID) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Group Chat
CREATE TABLE Group_Chat (
    ID INTEGER,
    Image BYTEA DEFAULT NULL,
    Description VARCHAR DEFAULT NULL,

    PRIMARY KEY (ID),
    FOREIGN KEY (ID) REFERENCES Team(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Message
CREATE TABLE Message (
    ID SERIAL,
    Content VARCHAR DEFAULT NULL,
    File BYTEA DEFAULT NULL,
    FileInfo JSON DEFAULT NULL,
    Sent_Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ID_Group INTEGER,
    ID_User INTEGER,

    CONSTRAINT MessageConstraint CHECK ((Content IS NULL AND File IS NOT NULL) OR (File IS NULL AND Content IS NOT NULL)),
    CONSTRAINT FileConstraint CHECK ((File IS NOT NULL AND FileInfo IS NOT NULL) OR (File IS NULL AND FileInfo IS NULL)),

    PRIMARY KEY (ID),
    FOREIGN KEY (ID_Group) REFERENCES Group_Chat(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_User) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Comment
CREATE TABLE Comment (
    ID SERIAL,
    Text VARCHAR NOT NULL,
    Sent_Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ID_Idea INTEGER,
    ID_User INTEGER,

    PRIMARY KEY (ID),
    FOREIGN KEY (ID_Idea) REFERENCES Idea(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_User) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Need
CREATE TABLE Need (
    ID_Idea INTEGER,
    ID_Skill INTEGER,

    PRIMARY KEY (ID_Idea, ID_Skill),
    FOREIGN KEY (ID_Idea) REFERENCES Idea(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_Skill) REFERENCES Skill(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Is_Related
CREATE TABLE Is_Related (
    ID_Idea INTEGER,
    ID_Topic INTEGER,

    PRIMARY KEY (ID_Idea, ID_Topic),
    FOREIGN KEY (ID_Idea) REFERENCES Idea(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_Topic) REFERENCES TOPIC(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Has
CREATE TABLE Has (
    ID_User INTEGER,
    ID_Skill INTEGER,
    Level INTEGER DEFAULT 1 NOT NULL CHECK (Level BETWEEN 1 AND 5),

    PRIMARY KEY (ID_User, ID_Skill),
    FOREIGN KEY (ID_User) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_Skill) REFERENCES Skill(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Follow
CREATE TABLE Follow (
    ID_User INTEGER,
    ID_Topic INTEGER,

    PRIMARY KEY (ID_User, ID_Topic),
    FOREIGN KEY (ID_User) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_Topic) REFERENCES TOPIC(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Likes
CREATE TABLE Likes (
    ID_Idea INTEGER,
    ID_User INTEGER,

    PRIMARY KEY (ID_User, ID_Idea),
    FOREIGN KEY (ID_Idea) REFERENCES Idea(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_User) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Request
CREATE TABLE Request (
    ID_Team INTEGER,
    ID_User INTEGER,
    Status STATUSTYPE NOT NULL DEFAULT 'Pending',
    Message VARCHAR(250) DEFAULT NULL,
    Requested_Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (ID_User, ID_Team),
    FOREIGN KEY (ID_User) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ID_Team) REFERENCES Team(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Login Tokens
CREATE TABLE Tokens (
    ID_Token UUID NOT NULL,
    ID_User INTEGER NOT NULL,
    Creation_Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (ID_Token),
    FOREIGN KEY (ID_User) REFERENCES Users(ID) ON UPDATE CASCADE ON DELETE CASCADE
);