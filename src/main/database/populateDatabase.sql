-- Users
INSERT INTO Users (Username, Email, Password, Role, Banned) VALUES
   ('FlamingoMark', 'marcoalecci98@gmail.com', 'd67d9fc418505ea8115c148646dc38d5', 'Administrator', False),
   ('elia.ziroldo', 'eliazir@gmail.com', '583cb78aaed75c23217df86262f9fb84', 'Administrator', False),
   ('luca.martinelli.09', 'martinelliluca98@gmail.com', '326da073d682af297a48ec8d05b4d8d9', 'Administrator', False),
   ('mario__RoSSi', 'mariossi@gmail.com', 'b6aba2967ef94d1b5c286ee0bf1f3fe5', 'Moderator', False),
   ('miriam_kappello', 'miriamkap@gmail.com', '7726666105a1876901fa74c75ca14cc6', 'Common User', False),
   ('FilippooIV', 'filippo.matt@gmail.com', 'bca5d66757850bc9946bf5f3f8903b23', 'Common User', False),
   ('gre.vincenzini', 'greta.vince@gmail.com', '9d5ed1ff47eb73b045a3eec52a1731a8', 'Moderator', False),
   ('luci.crosa', 'lucia.crosariol@gmail.com', '5dca73a0ae2e15c5791ccc93ad9642bf', 'Common User', False),
   ('ale.crosa', 'alessandra.crosariol@libero.it', '3c9560912ff466a8ed9920f1297496c3', 'Common User', False),
   ('Mattyfi', 'matteo.yufi@gmail.com', 'e49ac6c6e682533038e20a59aa2e008a', 'Common User', False),
   ('Marta_Esposito', 'marta.esposito@gmail.com', '487a25d41096a55b16013401fc2c00a2', 'Common User', True);
 
-- Profile
INSERT INTO Profile (ID, Name, Surname, BirthDate, Gender) VALUES
   (1, 'Marco', 'Alecci', '1998-11-26', 'Male'),
   (2, 'Elia', 'Ziroldo', '1998-08-21', 'Male'),
   (3, 'Luca', 'Martinelli', '1998-09-24', 'Male'),
   (4, 'Mario', 'Rossi', '1962-05-02', 'Male'),
   (5, 'Miriam', 'Kappello', '1998-07-12', 'Female'),
   (6, 'Filippo', 'Mattena', '1995-09-13', 'Male'),
   (7, 'Greta', 'Vincenzini', '1960-11-01', 'Female'),
   (8, 'Lucia', 'Crosariol', '1975-06-01', 'Female'),
   (9, 'Alessandra', 'Crosariol', '1968-02-22', 'Female'),
   (10, 'Matteo', 'Yufi', '1980-03-20', 'Male'),
   (11, 'Marta', 'Esposito', '1977-10-26', 'Female');
 
-- Skill
INSERT INTO Skill (Name, Description) VALUES
   ('Java', 'Knowledge of the Java programming language'),
   ('Python', 'Knowledge of the Python programming language'),
   ('C++', 'Knowledge of the C++ programming language'),
   ('PHP', 'Knowledge of the PHP programming language'),
   ('JavaScript', 'Knowledge of the JavaScript programming language'),
   ('HTML', 'Knowledge of the HTML language'),
   ('CSS', 'Knowledge of the CSS language'),
   ('SQL', 'Knowledge of the SQL  language'),
   ('Italian', 'Knowledge of the Italian language'),
   ('English', 'Knowledge of the English language'),
   ('French', 'Knowledge of the French language'),
   ('Spanish', 'Knowledge of the Spanish language'),
   ('German', 'Knowledge of the German language'),
   ('Problem Solving', 'Analytic and rational approach to problems'),
   ('Team Work', 'Capacity to work in groups'),
   ('Leadership', 'Capacity to supervise and direct other people'),
   ('Microsoft Office', 'Capacity to use programs of Microsoft Office Suite like Word, Excel, Powerpoint, etc...'),
   ('Adobe Photoshop', 'Ability to use Adobe Photoshop'),
   ('Adobe Illustrator', 'Ability to use Adobe Illustrator'),
   ('Adobe After Effects', 'Ability to use Adobe After Effects'),
   ('Adobe Premiere', 'Ability to use Adobe Premiere');
 
-- Topic
INSERT INTO Topic (Name, Description) VALUES
   ('Machine Learning', 'Computer algorithms that improve automatically through experience'),
   ('Movies', 'The seventh art'),
   ('Cooking', 'The practice or skill of preparing food by combining, mixing, and heating ingredients'),
   ('Statistic', 'Collection, organization, analysis, interpretation and presentation of data'),
   ('Audio Engineering', 'Study of sound and vibration, creating complex sound experience'),
   ('Photo & Video', 'Creating and editing any type of visual media'),
   ('Languages', 'Study and developing of languages around the world'),
   ('History', 'Study of how our world has evolved'), 
   ('Health and wellness', 'Everything concerning a better life for humans'),
   ('Telecommunications', 'Satellite communications, WI-Fi, Bluetooth'),
   ('Embedded Devices', 'Android, iOS devices, smart TVs, router...'),
   ('Finance', 'All that regards money and commerce'),
   ('Automotive', 'Cars and everything that regard them, like the mechanical or the electrical parts'),
   ('Databases', 'organized collection of data, stored and accessed electronically from a computer system'),
   ('Video Games', 'Any type of media that require a strong interaction by the user'),
   ('Web Application', 'Computer programs that utilizes web browsers and web technology to perform tasks over the Internet');
 
-- Idea
INSERT INTO Idea (Title, Description, Posted_Time, Num_Likes, ID_Creator) VALUES
   ('HyperU', 'An application to share ideas', '2020-10-05 13:00:00', 6, 1),
   ('CookMaster', 'A new mobile app to share new original dishes', '2020-07-15 11:45:00', 0, 3), 
   ('DriveToSurvive', 'A new video game that combine shooting and driving in an apocalyptic scenario', '2020-09-05 17:36:00', 3, 11), 
   ('Staywell', 'A project for developing an healthy way of life', '2019-11-07 09:24:00', 0, 5), 
   ('Esperanto', 'Studying and building a new language for european countries', '2018-04-03 08:23:00', 0, 6),
   ('SuperCharge', 'A new project for a unique charger of mobile phone and computers', '2019-06-08 18:45:00', 4, 6),
   ('OnePlus', 'A new mobile phone high tech startup', '2020-03-23 21:23:00', 0, 6),
   ('Light in the dark', 'A new indie horror movie setted in Catania, Italy', '2019-12-12 22:54:00', 0, 7),
   ('SafePlus', 'A new revolutionary web project for helping people to invest well their money', '2020-09-18 15:18:36', 0, 9);
 
--Team
INSERT INTO Team (Name, Creation_Time, ID_Idea) VALUES
   ('HyperGroup', '2020-10-06 17:00:00', 1),
   ('ChargersCrew', '2019-06-12 15:18:36', 6),
   ('PillarsMen', '2020-09-26 17:00:00', 3);
 
 
--Group Chat
INSERT INTO Group_Chat (ID, Description) VALUES
   (1, 'The chat of the HyperGroup!'),
   (2, 'The chat of the ChargerCrew!'),
   (3, 'The chat of PillarsMen!');
 
-- Message
--For the examples we take the group chat of the HyperU Idea between users 1, 2, 3
INSERT INTO Message (Content, Sent_Time, ID_Group, ID_User) VALUES
   ('I think this is a good Idea ', '2020-10-07 15:00:00', 1, 3),
   ('Yes, for me too', '2020-10-07 15:12:00', 1, 2),
   ('I am so glad you liked it!', '2020-10-07 15:16:00', 1, 1),
   ('We also need to create a database for the application', '2020-10-07 15:17:00', 1, 1),
   ('Ok, no problem I followed a database course last year', '2020-10-07 15:21:00', 1, 2),
   ('Nice!', '2020-10-07 15:24:00', 1, 1),
   ('Maybe, I can take care of the design', '2020-10-07 15:28:00', 1, 3),
   ('Yeah, sure!', '2020-10-07 15:31:00', 1, 1);
 
-- Comment
INSERT INTO Comment (Text, Sent_Time, ID_Idea, ID_User) VALUES
   ('Seems a good idea!','2020-10-05 15:12:00', 1, 5),
   ('Wow!','2020-10-05 16:43:00', 1, 6),
   ('Great!','2020-10-05 18:15:36', 1, 7),
   ('Nice Idea!','2020-10-05 15:12:00', 3, 8),
   ('This does not sound like a good idea!','2020-10-05 16:43:00', 6, 2),
   ('It will be such a beautiful game!','2020-10-05 18:15:36', 3, 2);
 
-- Need
INSERT INTO Need (ID_Idea, ID_Skill) VALUES
   (1, 6),
   (1, 7),
   (1, 14),
   (1, 21),
   (5, 8),
   (5, 9),
   (5, 10),
   (5, 11),
   (5, 12);
 
-- Is_Related
INSERT INTO Is_Related (ID_Idea, ID_Topic) VALUES
   (1, 14),
   (1, 16),
   (5, 7),
   (7, 11),
   (3, 15);
 
-- Has
-- For the examples we only use users 1, 2, 3
INSERT INTO Has (Id_User, Id_Skill, Level) VALUES
   --Skill of User 1 
   (1, 1, 4),
   (1, 2, 3),
   (1, 8, 5),
   (1, 9, 4),
   (1, 16, 5),
   (1, 15, 4),
   (1, 21, 4),
   --Skill of User 2   
   (2, 1, 3),
   (2, 8, 5),
   (2, 9, 3),
   (2, 16, 4),
   (2, 14, 3),
   (2, 21, 3),
   --Skill of User 3
   (3, 1, 5),
   (3, 17, 4),
   (3, 8, 5),
   (3, 9, 4),
   (3, 19, 3),
   (3, 4, 4),
   (3, 13, 4),
   (3, 6, 4),
   (3, 7, 4);
 
-- Follow
-- For the examples we only use users 1,2,3
INSERT INTO Follow (Id_User, Id_Topic) VALUES
   --Interests of User 1 
   (1, 11),
   (1, 15),
   (1, 14),
   (1, 12),
   --Interests of User 2  
   (2, 2),
   (2, 15),
   (2, 13),
   (2, 10),
   --Interests of User 3
   (3, 6),
   (3, 5),
   (3, 11),
   (3, 10);
 
-- Likes
INSERT INTO Likes (Id_Idea, Id_User) VALUES
   (1, 2),
   (1, 3),
   (1, 4),
   (1, 5),
   (1, 6),
   (1, 8),
   (3, 1),
   (3, 5),
   (3, 7),
   (6, 2),
   (6, 3),
   (6, 8),
   (6, 9);
 
-- Request
INSERT INTO Request (Id_Team, Id_User, Status, Message, Requested_Time) VALUES
   (1, 2, 'Accepted', 'I know sql, can I join?','2020-10-07 11:00:00'),
   (1, 3, 'Accepted', 'It seems a good idea, can I be a part of the group?','2020-10-07 11:30:00'),
   (2, 5, 'Pending', 'Can I be a part of the group?','2020-09-06 14:15:00'),
   (3, 8, 'Pending', 'Can I join?','2019-06-12 14:24:00');