INSERT INTO fraud_alerts (id, voucher_code, partner_id, alert_type, severity_level, detection_details, detected_at, status) VALUES
(1, 'GLOBAL-B2B-A1', 2, 'MULTIPLE_REDEMPTION_ATTEMPT', 4, 'Multiple redemption attempts from different IP addresses within 10 minutes.', '2026-04-12 15:30:00', 'INVESTIGATION_PENDING'),
(2, 'TECH-SUMMER-01', 1, 'BRUTE_FORCE', 5, '30 failed attempts to guess voucher code suffix for Tech Summer promo.', '2026-04-13 09:15:00', 'CONFIRMED'),
(3, 'EDU-STUDENT-99', 3, 'DUPLICATE_USAGE', 2, 'Same student ID attempted to use the same voucher code twice.', '2026-04-11 11:20:00', 'DISMISSED'),
(4, 'GLOBAL-B2B-B2', 2, 'UNAUTHORIZED_TRANSFER', 3, 'Voucher code assigned to corporate email was verified from a public email domain.', '2026-04-14 08:00:00', 'INVESTIGATION_PENDING'),
(5, 'TECH-SUMMER-02', 1, 'MULTIPLE_REDEMPTION_ATTEMPT', 5, 'Same code redeemed 7 times within 2 hours from different merchant locations in multiple cities.', '2026-04-15 14:45:00', 'CONFIRMED'),
(6, 'EDU-STUDENT-100', 3, 'BRUTE_FORCE', 3, 'Sequential brute force attempts on student discount codes over 3-hour period with 15 failures.', '2026-04-16 10:30:00', 'INVESTIGATION_PENDING'),
(7, 'AFRICA-LEADERSHIP-A', 5, 'UNAUTHORIZED_TRANSFER', 4, 'Leadership certification voucher transferred from registered corporate entity to personal account.', '2026-04-14 16:20:00', 'DISMISSED'),
(8, 'DIGITAL-TRANSFORM-001', 4, 'DUPLICATE_USAGE', 2, 'Corporate training code used by multiple departments simultaneously indicating account sharing.', '2026-04-13 13:15:00', 'OPEN');
