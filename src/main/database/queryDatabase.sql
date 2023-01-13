-- QUERY 1: GROUP CHAT (messages and username in a group chat)
SELECT
	Message.Sent_Time,
	Message.Content,
	Message.File,
	Users.Username
FROM
	Message
	INNER JOIN Users ON Message.ID_User = Users.ID
WHERE
	Message.ID_Group = 1
ORDER BY
	Message.Sent_Time ASC
LIMIT
	30;

-- QUERY 2: HOME PAGE (Ideas with topics followed by the user, excluding ideas posted by banned users)
-- Subquery 1: retrieve ideas
SELECT
	Idea.*
FROM
	Idea
	INNER JOIN Is_Related ON Idea.ID = Is_Related.ID_Idea
	INNER JOIN Follow ON Is_Related.ID_Topic = Follow.ID_Topic
	INNER JOIN Users ON Follow.ID_User = Users.ID
WHERE
	Users.ID = 1;

-- Subquery 2: retrieve information about the creators (check if user is banned)
SELECT
	DISTINCT Users.ID,
	Users.Username
FROM
	Idea
	INNER JOIN Users ON Idea.ID_Creator = Users.ID
WHERE
	Users.Banned = False;

-- Full query
SELECT
	Ideas.*,
	Creator.Username
FROM
	(
		SELECT
			Idea.*
		FROM
			Idea
			INNER JOIN Is_Related ON Idea.ID = Is_Related.ID_Idea
			INNER JOIN Follow ON Is_Related.ID_Topic = Follow.ID_Topic
			INNER JOIN Users ON Follow.ID_User = Users.ID
		WHERE
			Users.ID = 1
	) AS Ideas
	INNER JOIN (
		SELECT
			DISTINCT Users.ID,
			Users.Username
		FROM
			Idea
			INNER JOIN Users ON Idea.ID_Creator = Users.ID
		WHERE
			Users.Banned = False
	) AS Creator ON Ideas.ID_Creator = Creator.ID
LIMIT
	30;

-- Alternative method (without using subqueries)
SELECT
	Idea.ID,
	Idea.Title,
	Idea.Description,
	Idea.Image,
	Idea.Posted_Time,
	Idea.Num_Likes,
	Creator.Username
FROM
	Idea
	INNER JOIN Is_Related ON Idea.ID = Is_Related.ID_Idea
	INNER JOIN Follow ON Is_Related.ID_Topic = Follow.ID_Topic
	INNER JOIN Users ON Follow.ID_User = Users.ID
	INNER JOIN Users AS Creator ON Idea.ID_Creator = Creator.ID
WHERE
	Users.ID = 1
	AND Creator.Banned = False
ORDER BY
	Idea.Posted_Time
LIMIT
	30;

-- QUERY 3: USER PROFILE (infos, total likes, ideas posted)
SELECT
	Users.ID,
	Users.Username,
	Profile.Name,
	Profile.Surname,
	Profile.Gender,
	Profile.Birthdate,
	Profile.Profile_Picture,
	Profile.Biography,
	COUNT(Users.ID) AS Num_Ideas,
	SUM(Idea.Num_Likes) AS Total_Likes
FROM
	Users
	INNER JOIN Profile ON Users.ID = Profile.ID
	INNER JOIN Idea ON Idea.ID_Creator = Users.ID
WHERE
	Users.ID = 6
GROUP BY
	Users.ID,
	Profile.ID;

-- QUERY 4: IDEA (info and username) with an above average number of likes (excluding ideas posted by banned users)
SELECT
	Users.Username,
	Idea.Title,
	Idea.Description,
	Idea.Image,
	Idea.Num_Likes,
	Idea.Posted_Time
FROM
	Idea
	INNER JOIN Users ON Users.ID = Idea.ID_Creator
WHERE
	Users.Banned = False
	AND Idea.Num_Likes >= (
		SELECT
			AVG(Num_Likes)
		FROM
			Idea
			INNER JOIN Users ON Users.ID = Idea.ID_Creator
		WHERE
			Users.Banned = False
	);