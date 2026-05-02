INSERT INTO partner (id, name, contact_email, contact_phone) VALUES
(1, 'Tech Corp', 'info@techcorp.com', '+216 22 333 444'),
(2, 'Global Innovations', 'contact@globalinnovations.tn', '+216 55 666 777'),
(3, 'EduTech Solutions', 'support@edutech.tn', '+216 99 888 777'),
(4, 'Digital Solutions Inc', 'partnerships@digitalsol.tn', '+216 71 123 456'),
(5, 'Innovation Hub Africa', 'business@innovationhub.tn', '+216 95 789 012');

INSERT INTO deal (id, title, description, partner_id, start_date, end_date) VALUES
(1, 'Summer Tech Promo', 'Huge discounts on technology certifications', 1, '2026-06-01', '2026-08-31'),
(2, 'B2B Innovation Package', 'Special enterprise bundle for startup accelerators', 2, '2026-01-01', '2026-12-31'),
(3, 'Student Excellence Program', 'Discounted certification paths for students', 3, '2026-09-01', '2027-06-30'),
(4, 'Spring Digital Transformation', 'Enterprise digital skills development program', 4, '2026-04-01', '2026-06-30'),
(5, 'Africa Tech Leadership Initiative', 'Leadership and management certifications', 5, '2026-04-15', '2026-12-31');

INSERT INTO pack (id, name, description, validity_months, active) VALUES
(1, 'Starter Pack', 'Basic access to foundational courses', 6, true),
(2, 'Pro Pack', 'Full access including advanced lab environments', 12, true),
(3, 'Enterprise Ultimate', 'Unlimited access with priority 24/7 support', 24, true);

INSERT INTO access_code (id, code, partner_id, deal_id, expiration_date, used) VALUES
(1, 'TECH-SUMMER-01', 1, 1, '2026-08-31', false),
(2, 'TECH-SUMMER-02', 1, 1, '2026-08-31', true),
(3, 'TECH-SUMMER-03', 1, 1, '2026-08-31', false),
(4, 'GLOBAL-B2B-A1', 2, 2, '2026-12-31', false),
(5, 'GLOBAL-B2B-A2', 2, 2, '2026-12-31', true),
(6, 'GLOBAL-B2B-B2', 2, 2, '2026-12-31', false),
(7, 'EDU-STUDENT-99', 3, 3, '2027-06-30', false),
(8, 'EDU-STUDENT-100', 3, 3, '2027-06-30', true),
(9, 'DIGITAL-TRANSFORM-001', 4, 4, '2026-06-30', false),
(10, 'DIGITAL-TRANSFORM-002', 4, 4, '2026-06-30', false),
(11, 'AFRICA-LEADERSHIP-A', 5, 5, '2026-12-31', false),
(12, 'AFRICA-LEADERSHIP-B', 5, 5, '2026-12-31', false);
