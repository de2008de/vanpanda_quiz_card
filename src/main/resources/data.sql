INSERT INTO study_cards (id, title, subtitle, school) VALUES (1, 'ECON 103', 'Chapter 1: Demand and Supply', 'sfu');
INSERT INTO study_cards (id, title, subtitle, school) VALUES (2, 'ECON 105', 'Chapter 1: Demand and Supply', 'sfu');
INSERT INTO study_cards (id, title, subtitle, school) VALUES (3, 'ECON 105', 'Chapter 2: Marginal Cost', 'sfu');

INSERT INTO concept_cards (id, title) VALUES (1, 'What is Demand definition?');
INSERT INTO concept_cards (id, title) VALUES (2, 'What is Demand definition?');
INSERT INTO concept_cards (id, title) VALUES (3, 'What is Demand definition?');

INSERT INTO concept_cards (id, title) VALUES (4, 'What is Demand definition?');
INSERT INTO concept_cards (id, title) VALUES (5, 'What is Demand definition?');
INSERT INTO concept_cards (id, title) VALUES (6, 'What is Demand definition?');

INSERT INTO concept_cards (id, title) VALUES (7, 'What is Demand definition?');
INSERT INTO concept_cards (id, title) VALUES (8, 'What is Demand definition?');

INSERT INTO concept_cards (id, title) VALUES (9, 'What is Demand definition?');
INSERT INTO concept_cards (id, title) VALUES (10, 'What is Demand definition?');

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 1);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 2);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (1, 3);

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 4);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 5);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (2, 6);

INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (3, 9);
INSERT INTO STUDY_CARDS_CONCEPT_CARDS (study_card_id, concept_cards_id) VALUES (3, 10);

INSERT INTO key_points (id, content) VALUES (1, 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO key_points (id, content) VALUES (2, 'Demand can mean either market demand for a specific good or aggregate demand for the total of all goods in an economy.');
INSERT INTO key_points (id, content) VALUES (3, 'Demand, along with supply, determines the actual prices of goods and the volume of goods that changes hands in a market.');

INSERT INTO key_points (id, content) VALUES (4, 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO key_points (id, content) VALUES (5, 'Demand can mean either market demand for a specific good or aggregate demand for the total of all goods in an economy.');
INSERT INTO key_points (id, content) VALUES (6, 'Demand, along with supply, determines the actual prices of goods and the volume of goods that changes hands in a market.');

INSERT INTO key_points (id, content) VALUES (7, 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO key_points (id, content) VALUES (8, 'Demand can mean either market demand for a specific good or aggregate demand for the total of all goods in an economy.');
INSERT INTO key_points (id, content) VALUES (9, 'Demand, along with supply, determines the actual prices of goods and the volume of goods that changes hands in a market.');

INSERT INTO key_points (id, content) VALUES (10, 'Demand refers to consumers'' desire to purchase goods and services at given prices.');
INSERT INTO key_points (id, content) VALUES (11, 'Demand can mean either market demand for a specific good or aggregate demand for the total of all goods in an economy.');

INSERT INTO key_points (id, content) VALUES (12, 'Demand refers to consumers'' desire to purchase goods and services at given prices.');

INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (1, 1);
INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (1, 2);
INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (2, 3);

INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (2, 4);
INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (3, 5);
INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (4, 6);

INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (5, 7);
INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (6, 8);

INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (7, 9);
INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (8, 10);

INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (9, 11);
INSERT INTO CONCEPT_CARDS_KEY_POINTS (concept_card_id, key_points_id) VALUES (10, 12);

