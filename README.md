# news-app
This is an News App using HackerNews API using springboot
It use the Hacker News API available at Link to build the API backend for the users.

`/best-stories -  returns the top 10 best stories ranked by score in the last 15 minutes (Read Instructions), Each story should have title, URL, score, time of submission and the user who submitted it.`

`/past-stories -  returns all the past top stories that were served previously.`

`/comments/{story-id}-  returns the top 10 comments on a given story sorted by total number of child comments. Each comment should contain comment text, user’s hacker news handle and how old the users hacker news profile is in years`

# How to run the app
## Using docker
docker build -t news-app .

docker-compose up

## Stand-alone
java -jar news-app.jar

## How to test API
 Using postman/browser i.e. localhost:8080

