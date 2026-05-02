INSERT IGNORE INTO performance_alerts (id, partner_id, type, severity, message, status, created_at) VALUES
(1, 1, 'LOW_REDEMPTION', 'MEDIUM', 'Partner Tech Corp has 15% voucher redemption rate this month vs 40% average.', 'OPEN', '2026-04-10T10:00:00'),
(2, 2, 'NO_SALES', 'HIGH', 'Global Innovations has zero deals initiated in the last 60 days.', 'OPEN', '2026-04-12T09:30:00'),
(3, 3, 'TARGET_MET', 'LOW', 'EduTech Solutions reached their quarterly sales target early.', 'RESOLVED', '2026-03-25T14:00:00'),
(4, 4, 'LOW_ENGAGEMENT', 'MEDIUM', 'Digital Solutions Inc showing 25% lower engagement rates compared to peer partners.', 'OPEN', '2026-04-14T11:00:00'),
(5, 5, 'GROWTH_TREND', 'LOW', 'Innovation Hub Africa demonstrating consistent month-over-month growth of 18% in partnerships.', 'RESOLVED', '2026-04-08T13:30:00'),
(6, 1, 'SUCCESS_MILESTONE', 'LOW', 'Tech Corp surpassed 50,000 cumulative certifications issued through ForMe platform.', 'RESOLVED', '2026-04-12T15:45:00'),
(7, 2, 'PERFORMANCE_IMPROVEMENT', 'MEDIUM', 'Global Innovations improving redemption rates by 12% month-over-month after recent campaign refresh.', 'OPEN', '2026-04-13T10:20:00');
