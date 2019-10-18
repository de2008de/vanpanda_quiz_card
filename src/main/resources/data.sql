INSERT INTO users (id, username, email, password) VALUES (1, 'david', 'david@gmail.com', '123');

INSERT INTO study_cards (id, title, description, school, user_id) VALUES (1, 'ECON 103', 'Chapter 1: Demand and Supply', 'sfu', 1);
INSERT INTO study_cards (id, title, description, school, user_id) VALUES (2, 'ECON 105', 'Chapter 1: Demand and Supply', 'sfu', 1);
INSERT INTO study_cards (id, title, description, school, user_id) VALUES (3, 'ECON 105', 'Chapter 2: Marginal Cost', 'sfu', 1);

INSERT INTO concept_cards (id, title, content) VALUES (1, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (2, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (3, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (4, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (5, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (6, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (7, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (8, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (9, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO concept_cards (id, title, content) VALUES (10, 'What is Demand definition?', 'Demand refers to consumers'' desire to purchase goods and services at given prices.');

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 1);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 2);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 3);

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 4);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 5);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 6);

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (3, 9);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (3, 10);
