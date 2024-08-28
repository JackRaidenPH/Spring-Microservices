!!!
docker folder contains docker-compose.yaml for launching the whole system, as well as built images for each service. Images are large, so it might be better to build them yourself in order to save traffic.
!!!

Order\
Config-Server -> Auth-Server <-> Gateway <-> Filter-Service <-> Analytics-Service <-> Post-Service

Launch\
docker compose up

Auth API\
localhost:8000/api/auth/register - Register a new user via a JSON body(Keys: "username", "email", "password"), returns a JWT token\
localhost:8000/api/auth/login - Login using a username or an email via a JSON body(Keys: "username", "password"), returns a JWT token\
NOT EXPOSED, USED INTERNALLY FOR INTER-SYSTEM COMMUNICATION\
localhost:8001/public/rsa - Returns a Base64-encoded public RSA key string for JWT(JWS) validation

Posts API
localhost:8000/api/posts/add - Add post, structure - JSON: "userId" - long, "contents" - text\
localhost:8000/api/posts/all - All posts\
localhost:8000/api/posts/{id} - Post by id\
localhost:8000/api/posts/user/{id} - Post by user id

Analytics API:\
localhost:8000/api/analytics/bad_words_summary - Bad words statistics\
localhost:8000/api/analytics/record/{id} - Record id details\
localhost:8000/api/analytics/user/{id} - User statistics(approved / rejected)\
localhost:8000/api/analytics/summary - Overall statistics(total / approved / rejected)

Filters API:\
localhost:8000/api/filter/bad_words - All bad words\
localhost:8000/api/filter/check_text - Check a block of text supplied in JSON body(Key: "text"), return bad words matches\
localhost:8000/api/filter/is_bad?word={word} - Check a word supplied as a request parameter(Key: "word"), returns a single boolean\
localhost:8000/api/filter/add_bad_word?badWord={word} - Adds a word supplied as a request parameter(Key: "badWord")\
localhost:8000/api/filter/remove_bad_word?badWord={word} - Removes a word supplied as a request parameter(Key: "badWord")

![Diagram](https://github.com/user-attachments/assets/234407a6-2e5b-4f80-b46f-eca786772620)
