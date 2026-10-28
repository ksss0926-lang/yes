-- 취향 표 만들기 (mysql -u appuser -p appdb < db/schema.sql 로 실행)
-- 내 주제에 맞게 표 이름과 칸 이름을 바꿔도 된다. 바꾸면 FavoriteController.java 의 SQL 도 같이 바꾼다.
CREATE TABLE IF NOT EXISTS favorites (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  reason VARCHAR(200),
  rating INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO favorites (name, reason, rating) VALUES
  ('좋아하는 노래', '들으면 힘이 나서', 5),
  ('좋아하는 게임', '친구랑 같이 해서', 4),
  ('좋아하는 음식', '언제 먹어도 맛있어서', 5);
