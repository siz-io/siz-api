API Specs
=========

# Create a token
```json
POST /tokens

{}
```

## Notes
- You need to provide a json in all call, event if it's an empty json

## Results
```json
HTTP/1.1 201 Created
Content-Type: application/json

{
   "tokens" : {
      "href" : "/tokens/J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje",
      "id" : "J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje"
   }
}
```


# Create an user
## Request
```json
POST /users
X-Access-Token: J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje

{
  "users": {
    "email" : "paul@example.com",
    "password" : "6b3a55e0261b0304143f805a24924d0c1c44524821305f31d9277843b8a10f4e",
    "username" : "paul.the.boss"
  }
}
```
## Notes
- password is a sha256(password+salt) (ask for the salt details)
- password is not stored the same way as it's received :)
- password is 6 characters or more (no characters limitation)
- you can create only one user per token

## Results
```json
HTTP/1.1 201 Created
Content-Type: application/json

{
  "users": {
     "id":"548bf7d0e3bfc67d4d7c2cb6",
     "email":"paul@example.com",
     "username" : "paul.the.boss",
     "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
  },
  "tokens": {
     "user" : {
        "id" : "548bf7d0e3bfc67d4d7c2cb6",
        "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
     },
     "id":"fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc"
  }
}
```
```json
HTTP/1.1 409 Conflict
Content-Type: application/json

{
   "errors": {
      "title":"An user with email paul@example.com already exists"
   }
}
```

```json
HTTP/1.1 400 Bad Request
Content-Type: application/json
{
    "errors" : [
        {"title":"error with field /email : invalid email address"},
        {"title":"error with field /username : should be from 2 to 20 characters and contains only 0-9a-zA-Z."},
        {"title":"error with field /password : invalid sha256"}
    ]
}
```

```json
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
   "errors": {
      "title":"Unknown token J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje"
   }
}
```

```json
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
   "errors" : {
      "title" : "An user is already logged on this token, discard this token and create a new one."
   }
}

```

# Create an user with facebook
## Request
```json
POST /users
X-Access-Token: J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje

{
  "users": {
    "email" : "paul@example.com",
    "facebookToken" : "CAADOJKDKJLDJMLDSKdsklokfdmlgdfpo878UNKJnkjnNKJNKJNIUG6T56789jtyR5YU7Hyuitusijfddslofj6789ihjnkjTYChg56789NJUhbvgtcyr67yuvgft",
    "username" : "paul.the.boss"
  }
}
```
## Notes
- email and username are optional
- you can create only one user per token
- the user is automaticaly logged when created

## Results
```json
HTTP/1.1 201 Created
Content-Type: application/json

{
  "users": {
     "id":"548bf7d0e3bfc67d4d7c2cb6",
     "email":"paul@example.com",
     "username" : "paul.the.boss",
     "href" : "/users/548bf7d0e3bfc67d4d7c2cb6",
     "facebookUserId" : "15678999767689"
  },
  "tokens": {
     "user" : {
        "id" : "548bf7d0e3bfc67d4d7c2cb6",
        "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
     },
     "id":"fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc"
  }
}
```
```json
HTTP/1.1 409 Conflict
Content-Type: application/json

{
   "errors": {
      "title":"An user with facebookUserId 15678999767689 already exists"
   }
}
```

```json
HTTP/1.1 400 Bad Request
Content-Type: application/json
{
    "errors" : [
        {"title":"error with field /email : invalid email address"},
        {"title":"error with field /password : invalid sha256"}
    ]
}
```

# Login by email
## Request
```json
PUT /tokens/J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje
X-Access-Token: J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje

{
  "users": {
    "email" : "paul@example.com",
    "password" : "6b3a55e0261b0304143f805a24924d0c1c44524821305f31d9277843b8a10f4e"
  }
}
```

## Notes
- you can only login one user per token

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "users": {
     "id" : "548bf7d0e3bfc67d4d7c2cb6",
     "email":"paul@example.com",
     "username" : "paul.the.boss",
     "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
  },
  "tokens": {
     "user" : {
              "id" : "548bf7d0e3bfc67d4d7c2cb6",
              "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
     },
     "id":"fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc"
  }
}
```
```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"No user account for this email"
   }
}
```
```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"Incorrect password"
   }
}
```

# Login by username
## Request
```json
PUT /tokens/J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje
X-Access-Token: J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje

{
  "users": {
    "username" : "paul.the.boss",
    "password" : "6b3a55e0261b0304143f805a24924d0c1c44524821305f31d9277843b8a10f4e"
  }
}
```

## Notes
- you can only login one user per token

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "users": {
     "id" : "548bf7d0e3bfc67d4d7c2cb6",
     "email":"paul@example.com",
     "username" : "paul.the.boss",
     "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
  },
  "tokens": {
     "user" : {
              "id" : "548bf7d0e3bfc67d4d7c2cb6",
              "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
     },
     "id":"fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc"
  }
}
```
```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"No user account for this username"
   }
}
```
```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"Incorrect password"
   }
}
```

# Login by facebook
## Request
```json
PUT /tokens/J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje
X-Access-Token: J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje

{
  "users": {
     "facebookToken" : "CAADOJKDKJLDJMLDSKdsklokfdmlgdfpo878UNKJnkjnNKJNKJNIUG6T56789jtyR5YU7Hyuitusijfddslofj6789ihjnkjTYChg56789NJUhbvgtcyr67yuvgft",
  }
}
```

## Notes
- you can only login one user per token

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "users": {
     "id" : "548bf7d0e3bfc67d4d7c2cb6",
     "email":"paul@example.com",
     "username" : "paul.the.boss",
     "href" : "/users/548bf7d0e3bfc67d4d7c2cb6",
     "facebookUserId" : "15678999767689"
  },
  "tokens": {
     "user" : {
              "id" : "548bf7d0e3bfc67d4d7c2cb6",
              "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
     },
     "id":"fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc"
  }
}
```

```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"No user account for the facebookUserId associated to this facebookToken"
   }
}
```

# Check if an email is already used
## Request

```
GET /emails/paul@example.com
X-Access-Token: J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje
```

## Results

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
    "emails": {
       "id" : "paul@example.com",
       "state" : "registered"
    }
}
```

```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"Email not found"
   }
}
```

# Check if an username is already used
## Request

```
GET /usernames/paul1
X-Access-Token: J1WAnckPPHm8jX8Abvc61VvVBY1cmqCnGSr46oUqvOY0MUsO4u0dhWlGipHHZaje
```

## Results

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
    "usernames": {
       "id" : "paul1",
       "state" : "registered"
    }
}
```

```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"Username not found"
   }
}
```

# Retrieve user information
## Request
```
GET /users/548bf7d0e3bfc67d4d7c2cb6
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
```

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "users": {
     "id" : "548bf7d0e3bfc67d4d7c2cb6",
     "email":"paul@example.com",
     "username" : "paul.the.boss",
     "href" : "/users/548bf7d0e3bfc67d4d7c2cb6"
  }
}
```
```json
HTTP/1.1 403 Forbidden
Content-Type: application/json

{
   "errors": {
      "title":"You can only retrieve the user associated with the token"
   }
}
```
```json
HTTP/1.1 404 Not Found
Content-Type: application/json

{
   "errors": {
      "title":"Unknown token fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc"
   }
}
```

# Retrieve stories
## Request
```
GET /stories
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
```

## Notes
- You can use "limit" params to choose the number of stories by request (default to 12)

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
   "stories" : [
      {
         "picture" : {
            "href" : "http://img.youtube.com/vi/SE5Ip60_HJk/0.jpg"
         },
         "boxes" : [
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "type" : "mp4",
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4"
                  }
               ]
            },
            {
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ],
               "width" : 350
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            }
         ],
         "id" : "14217625935409c9a46d40cc",
         "tags" : [
            "meaningful-videos",
            "news"
         ],
         "creationDate" : 1421762593540,
         "slug" : "the-stand-up-kid",
         "source" : {
            "id" : "SE5Ip60_HJk",
            "duration" : 187000,
            "type" : "youtube"
         },
         "title" : "The stand up kid",
         "href" : "/stories/14217625935409c9a46d40cc"
      }
   ]
}
```


# Retrieve a stories
## Request
```
GET /stories/14217625935409c9a46d40cc
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
```

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
   "stories" :
      {
         "picture" : {
            "href" : "http://img.youtube.com/vi/SE5Ip60_HJk/0.jpg"
         },
         "boxes" : [
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "type" : "mp4",
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4"
                  }
               ]
            },
            {
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ],
               "width" : 350
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            }
         ],
         "id" : "14217625935409c9a46d40cc",
         "tags" : [
            "meaningful-videos",
            "news"
         ],
         "creationDate" : 1421762593540,
         "slug" : "the-stand-up-kid",
         "source" : {
            "id" : "SE5Ip60_HJk",
            "duration" : 187000,
            "type" : "youtube"
         },
         "title" : "The stand up kid",
         "href" : "/stories/14217625935409c9a46d40cc"
      }
}
```

# Retrieve a story by slug
## Request
```
GET /stories?slug=the-stand-up-kid
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
```

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
   "stories" :
      {
         "picture" : {
            "href" : "http://img.youtube.com/vi/SE5Ip60_HJk/0.jpg"
         },
         "boxes" : [
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "type" : "mp4",
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4"
                  }
               ]
            },
            {
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ],
               "width" : 350
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            }
         ],
         "id" : "14217625935409c9a46d40cc",
         "tags" : [
            "meaningful-videos",
            "news"
         ],
         "creationDate" : 1421762593540,
         "slug" : "the-stand-up-kid",
         "source" : {
            "id" : "SE5Ip60_HJk",
            "duration" : 187000,
            "type" : "youtube"
         },
         "title" : "The stand up kid",
         "href" : "/stories/14217625935409c9a46d40cc"
      }
}
```

# Like/Nope a story
## Request
```json
POST /events HTTP/1.1
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc

{
   "events" : {
       "storyId": "14217643385589f1f819dc77",
       "type" : "nope"
   }
}
```

## Notes
- Type can be like or nope

## Results
```json
HTTP/1.1 201 Created
Content-Type: application/json

{
    "events": {
        "storyId": "14217643385589f1f819dc77",
        "id": "54f986a3590000ae0331438b",
        "date": 1425639075852,
        "type": "nope"
    }
}
```

# Retrieve liked stories
## Request
```json
GET /stories?filterBy=likes
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
```

## Request liked stories before the first Id you have
```json
GET /stories?filterBy=likes&sinceId=1421764247847062159db364
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
```

## Request liked stories after the last Id you have
```json
GET /stories?filterBy=likes&lastSkippedId=14217708191527cfae71d478
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
```

## Notes
- Results are ordered by last stories liked
- "sinceId" and "lastSkippedId" can be combined
- You can use "limit" params to choose the number of stories by request (default to 12)

## Results
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
   "stories" : [
      {
         "picture" : {
            "href" : "http://img.youtube.com/vi/SE5Ip60_HJk/0.jpg"
         },
         "boxes" : [
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "type" : "mp4",
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4"
                  }
               ]
            },
            {
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ],
               "width" : 350
            },
            {
               "width" : 350,
               "height" : 196,
               "formats" : [
                  {
                     "href" : "http://static.siz.io/sequences/788798Y1112d.mp4",
                     "type" : "mp4"
                  }
               ]
            }
         ],
         "id" : "14217625935409c9a46d40cc",
         "tags" : [
            "meaningful-videos",
            "news"
         ],
         "creationDate" : 1421762593540,
         "slug" : "the-stand-up-kid",
         "source" : {
            "id" : "SE5Ip60_HJk",
            "duration" : 187000,
            "type" : "youtube"
         },
         "title" : "The stand up kid",
         "href" : "/stories/14217625935409c9a46d40cc"
      }
   ]
}
```

# Generate new story
## Request

```json
POST /stories
X-Access-Token: fuEyvqImw2xbywewZAUHkFMo8xJO7eSOAOjkaRRSTTfzRTqdblN65Mx7O2JhmzVc
{   "stories" : [
        {
            "boxes": [
                {
                    "start": 2125,
                    "stop": 9500
                },
                {
                    "start": 15000,
                    "stop": 20375
                },
                {
                    "start": 27000,
                    "stop": 31625
                },
                {
                    "start": 35750,
                    "stop": 42375
                }
            ],
            "source": {
                "id": "xgy2a9tFSPU",
                "type": "youtube"
            },
            "title": "Introduce Tree Book Tree",
            "tags": [
                "meaningful-videos",
                "news"
            ]
        }
    ]
}
```

## Results

```json
{
    "stories": [
        {
            "boxes": [
                {
                    "start": 2125,
                    "stop": 9500,
                },
                {
                    "start": 15000,
                    "stop": 20375,
                },
                {
                    "start": 27000,
                    "stop": 31625,
                },
                {
                    "start": 35750,
                    "stop": 42375,
                }
            ],
            "creationDate": 1435185344038,
            "id": "558b30c06a00007b000f3176",
            "slug": "introduce-tree-book-tree",
            "source": {
                "id": "xgy2a9tFSPU",
                "type": "youtube"
            },
            "picture": {
                "href": "http://img.youtube.com/vi/xgy2a9tFSPU/0.jpg"
            },
            "title": "Introduce Tree Book Tree",
            "tags": [
                "meaningful-videos",
                "news"
            ],
            "href": "/stories/558b30c06a00007b000f3176"
        }
    ]
}
```
