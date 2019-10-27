INSERT INTO users (id, username, email, password) VALUES (1, 'david', 'david@gmail.com', '123');

INSERT INTO study_cards (id, title, description, school, user_id) VALUES (1, 'ECON 103', 'Chapter 1: Demand and Supply', 'sfu', 1);
INSERT INTO study_cards (id, title, description, school, user_id) VALUES (2, 'ECON 105', 'Chapter 1: Demand and Supply', 'sfu', 1);
INSERT INTO study_cards (id, title, description, school, user_id) VALUES (3, '水果英语', '日常英语精品卡片', null, 1);

INSERT INTO concept_cards (id, term, definition) VALUES (1, 'Demand', 'Refers to consumers desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, term, definition) VALUES (2, 'Government spending', 'Economists distinguish between government spending on newly produced goods and services, such as paying a company to build a new highway, and government spending on transfer payments, which are payments such as welfare payments intended to redistribute income.');
INSERT INTO concept_cards (id, term, definition) VALUES (3, 'Cost', 'The economics term cost, also known as economic cost or opportunity cost, refers to the potential gain that is lost by foregoing one opportunity in order to take advantage of another. The lost potential gain is the cost of the opportunity that is accepted.');
INSERT INTO concept_cards (id, term, definition) VALUES (4, 'Demand', 'Refers to consumers desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, term, definition) VALUES (5, 'Government spending', 'Economists distinguish between government spending on newly produced goods and services, such as paying a company to build a new highway, and government spending on transfer payments, which are payments such as welfare payments intended to redistribute income.');
INSERT INTO concept_cards (id, term, definition) VALUES (6, 'Cost', 'The economics term cost, also known as economic cost or opportunity cost, refers to the potential gain that is lost by foregoing one opportunity in order to take advantage of another. The lost potential gain is the cost of the opportunity that is accepted.');
INSERT INTO concept_cards (id, term, definition) VALUES (7, 'Demand', 'Refers to consumers desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, term, definition) VALUES (8, 'Government spending', 'Economists distinguish between government spending on newly produced goods and services, such as paying a company to build a new highway, and government spending on transfer payments, which are payments such as welfare payments intended to redistribute income.');
INSERT INTO concept_cards (id, term, definition) VALUES (9, 'Banana', '香蕉');
INSERT INTO concept_cards (id, term, definition) VALUES (10, 'Apple', '苹果');

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 1);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 2);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 3);

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 4);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 5);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 6);

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (3, 9);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (3, 10);
