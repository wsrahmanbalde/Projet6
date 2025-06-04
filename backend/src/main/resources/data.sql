USE mdd;
-- ========== SUBJECT ==========
INSERT INTO subject (id, name, description) VALUES
(1, 'Cloud', 'Étude approfondie des technologies cloud, incluant l infrastructure, les services et les meilleures pratiques pour le déploiement dapplications modernes.'),
(2, 'Sécurité Informatique', 'Principes, méthodes et outils pour protéger les systèmes informatiques contre les menaces et les attaques.'),
(3, 'Intelligence Artificielle', 'Exploration des algorithmes, apprentissage automatique et systèmes intelligents capables d effectuer des tâches complexes.'),
(4, 'Développement Web', 'Technologies front-end et back-end pour la création de sites et applications web performants et accessibles.'),
(5, 'Bases de Données', 'Conception, optimisation et gestion des bases de données relationnelles et NoSQL.');

-- ========== USER ==========
INSERT INTO user (id, email, username, password, role) VALUES
(1, 'alice@example.com', 'alice', 'passAlice123', 'USER'),
(2, 'bob@example.com', 'bob', 'passBob123', 'USER'),
(3, 'charlie@example.com', 'charlie', 'passCharlie123', 'USER'),
(4, 'danielle@example.com', 'danielle', 'passDanielle123', 'USER'),
(5, 'eve@example.com', 'eve', 'passEve123', 'ADMIN');

-- ========== SUBSCRIPTION ==========
INSERT INTO subscription (id, user_id, subject_id, subscription_date) VALUES
(1, 1, 1, '2025-05-01 10:00:00'),
(2, 1, 3, '2025-05-02 15:30:00'),
(3, 2, 2, '2025-05-03 08:45:00'),
(4, 3, 4, '2025-05-04 12:00:00'),
(5, 4, 5, '2025-05-05 14:20:00');

-- ========== POST ==========
INSERT INTO post (id, title, content, subject_id, author_id, created_at) VALUES
(1, 'Introduction au Cloud Computing', 'Le cloud computing révolutionne la manière dont nous déployons et gérons les infrastructures informatiques, offrant scalabilité et flexibilité.', 1, 1, '2025-05-01 11:00:00'),
(2, 'Les meilleures pratiques de sécurité', 'Découvrez les meilleures pratiques pour protéger vos applications contre les cyberattaques, incluant le chiffrement et la gestion des accès.', 2, 2, '2025-05-02 09:00:00'),
(3, 'Apprentissage supervisé vs non supervisé', 'Cet article explique la différence entre apprentissage supervisé et non supervisé, avec exemples d applications concrètes.', 3, 3, '2025-05-03 10:15:00'),
(4, 'Créer un site web responsive', 'Apprenez à créer des sites web qui s adaptent à tous les types d écrans grâce au responsive design.', 4, 4, '2025-05-04 13:30:00'),
(5, 'Optimisation des requêtes SQL', 'Conseils et techniques pour améliorer les performances de vos bases de données relationnelles à travers l optimisation des requêtes.', 5, 5, '2025-05-05 15:45:00');

-- ========== COMMENT ==========
INSERT INTO comment (id, content, post_id, author_id, created_at) VALUES
(1, 'Très bon article sur le cloud, merci pour ces explications claires !', 1, 2, '2025-05-01 12:00:00'),
(2, 'J ai toujours eu du mal avec la sécurité, cet article m a beaucoup aidé.', 2, 3, '2025-05-02 10:30:00'),
(3, 'Merci pour les exemples sur l IA, très instructif.', 3, 1, '2025-05-03 11:00:00'),
(4, 'Le responsive design est indispensable aujourd hui, merci pour ce guide.', 4, 5, '2025-05-04 14:00:00'),
(5, 'L optimisation SQL est souvent négligée, merci pour les conseils.', 5, 4, '2025-05-05 16:00:00');