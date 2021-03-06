package models

case class TopLevel(links: Option[Map[String,String]] = None,
                    emails: Option[Either[Email,Seq[Email]]] = None,
                    usernames: Option[Either[Username,Seq[Username]]] = None,
                    users: Option[User] = None,
                    tokens: Option[Token] = None,
                    stories: Option[Either[Story,Seq[Story]]] = None,
                    errors: Option[Either[Error,Seq[Error]]] = None,
                    events: Option[Event] = None
                     )