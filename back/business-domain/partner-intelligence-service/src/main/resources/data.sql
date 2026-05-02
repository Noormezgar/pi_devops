-- Anomaly Alerts
INSERT INTO anomaly_alert (partner_id, anomaly_type, severity_score, explanation, status, created_at) VALUES
(1, 'UNUSUAL_REDEMPTION_SPIKE', 0.87, 'Tech Corp experienced a 340% spike in voucher redemptions over the past 48 hours, significantly above their historical pattern. This could indicate a marketing campaign or potential fraud activity.', 'OPEN', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 86400)),
(2, 'LOW_ENGAGEMENT_PATTERN', 0.62, 'Global Innovations shows declining engagement metrics with only 22% of issued vouchers being redeemed in the last month, down from 65% average.', 'OPEN', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 172800)),
(3, 'GEOGRAPHIC_ANOMALY', 0.75, 'EduTech Solutions reporting usage patterns from IP addresses inconsistent with their registered territories, suggesting potential geographic expansion or account compromise.', 'RESOLVED', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 259200));

-- Forecast Snapshots
INSERT INTO forecast_snapshot (partner_id, forecast_period, metric_name, predicted_value, confidence_score, created_at) VALUES
(1, '2026-Q2', 'revenue', 15500.75, 0.92, NOW()),
(1, '2026-Q2', 'redemption_rate', 0.68, 0.85, NOW()),
(2, '2026-Q2', 'revenue', 42300.00, 0.88, NOW()),
(2, '2026-Q2', 'redemption_rate', 0.55, 0.79, NOW()),
(3, '2026-Q2', 'revenue', 8900.50, 0.91, NOW()),
(3, '2026-Q2', 'redemption_rate', 0.72, 0.87, NOW());

-- Deal Health Score Snapshots
INSERT INTO deal_health_score_snapshot (deal_id, partner_id, health_score, performance_category, factors_json, snapshot_date) VALUES
(1, 1, 0.78, 'HEALTHY', '{"redemption_rate": 0.72, "velocity": 0.85, "churn_risk": 0.15, "engagement": 0.80}', CURDATE()),
(2, 2, 0.65, 'MONITORING', '{"redemption_rate": 0.55, "velocity": 0.60, "churn_risk": 0.35, "engagement": 0.68}', CURDATE()),
(3, 3, 0.82, 'HEALTHY', '{"redemption_rate": 0.79, "velocity": 0.88, "churn_risk": 0.10, "engagement": 0.85}', CURDATE());

-- Partner Insight Summaries
INSERT INTO partner_insight_summary (partner_id, summary, language, created_at) VALUES
(1, 'Tech Corp shows strong engagement with 72% redemption rates and growing market presence. Recent spike in activity suggests successful marketing initiatives. Recommend maintaining current strategy while monitoring for unusual patterns.', 'en', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 3600)),
(2, 'Global Innovations demonstrates stable enterprise relationships with premium partnership value. However, market engagement has plateaued at 55% redemption rates. Opportunity exists for enhanced deal packaging and customer engagement initiatives.', 'en', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 7200)),
(3, 'EduTech Solutions leading in educational market segment with 79% redemption rates and high engagement metrics. Strong trajectory in student certification programs. Consider expanding enterprise training offerings to diversify revenue streams.', 'en', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 10800));

-- Recommendation Items
INSERT INTO recommendation_item (partner_id, recommendation_type, title, description, priority_level, predicted_impact, status, created_at) VALUES
(1, 'MARKETING_EXPANSION', 'Expand Summer Tech Promo Geographically', 'Current summer promotion shows 85% success rate in primary regions. Recommend expanding to secondary markets with localized messaging.', 'HIGH', 0.45, 'OPEN', NOW()),
(1, 'PARTNERSHIP_OPTIMIZATION', 'Negotiate Volume Discount Tier', 'Historical data shows 22% drop in average order value during discount periods. Recommend tier-based incentives to maintain margin integrity.', 'MEDIUM', 0.28, 'OPEN', NOW()),
(2, 'ENGAGEMENT_BOOST', 'Launch Customer Retention Program', 'Partner showing early signs of engagement decline. Implement loyalty rewards program targeting enterprise customers.', 'HIGH', 0.62, 'OPEN', NOW()),
(3, 'REVENUE_DIVERSIFICATION', 'Enter Corporate Training Market', 'Market analysis indicates strong demand for edu-tech corporate training. EduTech Solutions positioned to capture 35% of regional market share.', 'MEDIUM', 0.55, 'OPEN', NOW());

-- Inference Audit Logs
INSERT INTO inference_audit_log (partner_id, inference_type, model_name, input_data_summary, output_summary, execution_time_ms, status, created_at) VALUES
(1, 'ANOMALY_DETECTION', 'isolation_forest_v2', 'Last 30 days transaction history', 'Detected 1 anomaly (Redemption Spike)', 245, 'SUCCESS', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 3600)),
(2, 'DEMAND_FORECAST', 'arima_forecast_ensemble', 'Q1 2026 sales data, seasonal trends', 'Q2 2026 forecast: 42.3K revenue (88% confidence)', 812, 'SUCCESS', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 7200)),
(3, 'HEALTH_SCORE_CALCULATION', 'weighted_metrics_model', 'Partner engagement, redemption, churn metrics', 'Health Score: 0.82 (Healthy)', 156, 'SUCCESS', FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 10800));

-- Prompt Template Versions  
INSERT INTO prompt_template_version (template_name, version, template_content, model_type, status, created_at) VALUES
('partner_summary_writer', 1, 'Generate a business summary for partner {partner_name} based on metrics: redemption_rate={redemption_rate}, revenue={revenue}, engagement={engagement}.', 'GEMINI_PRO', 'ACTIVE', NOW()),
('anomaly_explainer', 1, 'Explain this sales anomaly: {anomaly_type} with severity {severity_score}. Provide business impact assessment and recommended actions.', 'GEMINI_PRO', 'ACTIVE', NOW()),
('recommendation_generator', 1, 'As a business analyst, provide 3 actionable recommendations for partner {partner_name} with current metrics: {metrics_json}. Prioritize by impact.', 'GEMINI_PRO', 'ACTIVE', NOW());
