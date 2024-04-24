



GitLab












Menu




Projects
Groups
Snippets















/








Help







Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab







Sign in





Toggle navigation

Menu








GitLab












Menu




Projects
Groups
Snippets















/








Help







Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab







Sign in





Toggle navigation

Menu






GitLab












Menu




Projects
Groups
Snippets



GitLab






GitLab




Menu




Projects
Groups
Snippets




Menu


Projects
Groups
Snippets












/








Help







Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab







Sign in













/











/








Help







Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab





Help



Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab




Help

Support

Community forum


Keyboard shortcuts
?


Submit feedback

Contribute to GitLab



Sign in


Sign in
Toggle navigation
Menu

Menu







Help

Help













GraphQL API (FREE)


Enabled and made generally available in GitLab 12.1. Feature flag graphql removed.

GraphQL is a query language for APIs. You can use it to
request the exact data you need, and therefore limit the number of requests you need.
GraphQL data is arranged in types, so your client can use
client-side GraphQL libraries
to consume the API and avoid manual parsing.
There are no fixed endpoints and no data model, so you can add
to the API without creating breaking changes.
This enables us to have a versionless API.

Vision
We want the GraphQL API to be the primary means of interacting
programmatically with GitLab. To achieve this, it needs full coverage - anything
possible in the REST API should also be possible in the GraphQL API.
To help us meet this vision, the frontend should use GraphQL in preference to
the REST API for new features.
There are no plans to deprecate the REST API. To reduce the technical burden of
supporting two APIs in parallel, they should share implementations as much as
possible.

Work with GraphQL
If you're new to the GitLab GraphQL API, see Get started with GitLab GraphQL API.
You can view the available resources in the GraphQL API reference.
The reference is automatically generated from the GitLab GraphQL schema and
written to a Markdown file.
The GitLab GraphQL API endpoint is located at /api/graphql.

GraphiQL
Explore the GraphQL API using the interactive GraphiQL explorer,
or on your self-managed GitLab instance on
https://<your-gitlab-site.com>/-/graphql-explorer.
For more information, see GraphiQL.

View GraphQL examples
You can work with sample queries that pull data from public projects on GitLab.com:

Create an audit report
Identify issue boards
Query users
Use custom emojis

The get started page includes different methods to customize GraphQL queries.

Breaking changes
The GitLab GraphQL API is versionless and changes to the API are primarily backward-compatible.
However, GitLab sometimes changes the GraphQL API in a way that is not backward-compatible. These changes are considered breaking changes, and
can include removing or renaming fields, arguments, or other parts of the schema.
When creating a breaking change, GitLab follows a deprecation and removal process.
Learn more about breaking changes.
Fields behind a feature flag and disabled by default do not follow the deprecation and removal process, and can be removed at any time without notice.
To avoid having a breaking change affect your integrations, you should
familiarize yourself with the deprecation and removal process.
WARNING:
GitLab makes all attempts to follow the deprecation and removal process.
On rare occasions, GitLab might make immediate breaking changes to the GraphQL
API to patch critical security or performance concerns if the deprecation
process would pose significant risk.

Deprecation and removal process
The deprecation and removal process for the GitLab GraphQL API aligns with the wider GitLab
deprecation process.
Parts of the schema marked for removal from the GitLab GraphQL API are first
deprecated
but still available for at least six releases. They are then removed
entirely during the next XX.0 major release.
Items are marked as deprecated in:

The schema.
The GraphQL API reference.
The deprecation feature removal schedule, which is linked from release posts.
Introspection queries of the GraphQL API.

NOTE:
If you use the GraphQL API, we recommend you remove the deprecated schema from your GraphQL
API calls as soon as possible to avoid experiencing breaking changes.
The deprecation message provides an alternative for the deprecated schema item,
if applicable.

Deprecation example
The following fields are deprecated in different minor releases, but both
removed in GitLab 14.0:



Field deprecated in
Reason




12.7
GitLab traditionally has 12 minor releases per major release. To ensure the field is available for 6 more releases, it is removed in the 14.0 major release (and not 13.0).


13.6
The removal in 14.0 allows for 6 months of availability.




List of removed items
View the list of items removed in previous releases.

Available queries
The GraphQL API includes the following queries at the root level:



Query
Description




project
Project information and many of its associations, such as issues and merge requests.


group
Basic group information and epics.


user
Information about a particular user.


namespace
The namespace and the projects in it.


currentUser
Information about the signed-in user.


users
Information about a collection of users.


metaData
Metadata about GitLab and the GraphQL API.


snippets
Snippets visible to the signed-in user.



New associations and root level objects are regularly added.
See the GraphQL API Reference for up-to-date information.
Root-level queries are defined in
app/graphql/types/query_type.rb.

Multiplex queries
GitLab supports batching queries into a single request using
@apollo/client/link/batch-http. More
information about multiplexed queries is also available for
GraphQL Ruby, the
library GitLab uses on the backend.

Limits
The following limits apply to the GitLab GraphQL API.



Limit
Default




Max page size
100 records (nodes) per page. Applies to most connections in the API. Particular connections may have different max page size limits that are higher or lower.


Max query complexity

200 for unauthenticated requests and 250 for authenticated requests.


Request timeout
30 seconds.




Max query complexity
The GitLab GraphQL API scores the complexity of a query. Generally, larger
queries have a higher complexity score. This limit is designed to protect
the API from performing queries that could negatively impact its overall performance.
You can query the complexity score of a query
and the limit for the request.
If a query exceeds the complexity limit, an error message response is
returned.
In general, each field in a query adds 1 to the complexity score, although
this can be higher or lower for particular fields. Sometimes, adding
certain arguments may also increase the complexity of a query.
NOTE:
The complexity limits may be revised in future, and additionally, the complexity
of a query may be altered.

Resolve mutations detected as spam

Introduced in GitLab 13.11.

GraphQL mutations can be detected as spam. If a mutation is detected as spam and:


A CAPTCHA service is not configured, a
GraphQL top-level error is raised. For example:

{
  "errors": [
    {
      "message": "Request denied. Spam detected",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "spam": true
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null
    }
  }
}




A CAPTCHA service is configured, you receive a response with:


needsCaptchaResponse set to true.
The spamLogId and captchaSiteKey fields set.

For example:

{
  "errors": [
    {
      "message": "Request denied. Solve CAPTCHA challenge and retry",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "needsCaptchaResponse": true,
        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",
        "spamLogId": 67
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null,
    }
  }
}




Use the captchaSiteKey to obtain a CAPTCHA response value using the appropriate CAPTCHA API.
Only Google reCAPTCHA v2 is supported.


Resubmit the request with the X-GitLab-Captcha-Response and X-GitLab-Spam-Log-Id headers set.


NOTE:
The GitLab GraphiQL implementation doesn't permit passing of headers, so we must write
this as a cURL query. --data-binary is used to properly handle escaped double quotes
in the JSON-embedded query.

export CAPTCHA_RESPONSE="<CAPTCHA response obtained from CAPTCHA service>"
export SPAM_LOG_ID="<spam_log_id obtained from initial REST response>"
curl --header "Authorization: Bearer $PRIVATE_TOKEN" --header "Content-Type: application/json" --header "X-GitLab-Captcha-Response: $CAPTCHA_RESPONSE" --header "X-GitLab-Spam-Log-Id: $SPAM_LOG_ID" --request POST --data-binary '{"query": "mutation {createSnippet(input: {title: \"Title\" visibilityLevel: public blobActions: [ { action: create filePath: \"BlobPath\" content: \"BlobContent\" } ] }) { snippet { id title } errors }}"}' "https://gitlab.example.com/api/graphql"













Help

Help













GraphQL API (FREE)


Enabled and made generally available in GitLab 12.1. Feature flag graphql removed.

GraphQL is a query language for APIs. You can use it to
request the exact data you need, and therefore limit the number of requests you need.
GraphQL data is arranged in types, so your client can use
client-side GraphQL libraries
to consume the API and avoid manual parsing.
There are no fixed endpoints and no data model, so you can add
to the API without creating breaking changes.
This enables us to have a versionless API.

Vision
We want the GraphQL API to be the primary means of interacting
programmatically with GitLab. To achieve this, it needs full coverage - anything
possible in the REST API should also be possible in the GraphQL API.
To help us meet this vision, the frontend should use GraphQL in preference to
the REST API for new features.
There are no plans to deprecate the REST API. To reduce the technical burden of
supporting two APIs in parallel, they should share implementations as much as
possible.

Work with GraphQL
If you're new to the GitLab GraphQL API, see Get started with GitLab GraphQL API.
You can view the available resources in the GraphQL API reference.
The reference is automatically generated from the GitLab GraphQL schema and
written to a Markdown file.
The GitLab GraphQL API endpoint is located at /api/graphql.

GraphiQL
Explore the GraphQL API using the interactive GraphiQL explorer,
or on your self-managed GitLab instance on
https://<your-gitlab-site.com>/-/graphql-explorer.
For more information, see GraphiQL.

View GraphQL examples
You can work with sample queries that pull data from public projects on GitLab.com:

Create an audit report
Identify issue boards
Query users
Use custom emojis

The get started page includes different methods to customize GraphQL queries.

Breaking changes
The GitLab GraphQL API is versionless and changes to the API are primarily backward-compatible.
However, GitLab sometimes changes the GraphQL API in a way that is not backward-compatible. These changes are considered breaking changes, and
can include removing or renaming fields, arguments, or other parts of the schema.
When creating a breaking change, GitLab follows a deprecation and removal process.
Learn more about breaking changes.
Fields behind a feature flag and disabled by default do not follow the deprecation and removal process, and can be removed at any time without notice.
To avoid having a breaking change affect your integrations, you should
familiarize yourself with the deprecation and removal process.
WARNING:
GitLab makes all attempts to follow the deprecation and removal process.
On rare occasions, GitLab might make immediate breaking changes to the GraphQL
API to patch critical security or performance concerns if the deprecation
process would pose significant risk.

Deprecation and removal process
The deprecation and removal process for the GitLab GraphQL API aligns with the wider GitLab
deprecation process.
Parts of the schema marked for removal from the GitLab GraphQL API are first
deprecated
but still available for at least six releases. They are then removed
entirely during the next XX.0 major release.
Items are marked as deprecated in:

The schema.
The GraphQL API reference.
The deprecation feature removal schedule, which is linked from release posts.
Introspection queries of the GraphQL API.

NOTE:
If you use the GraphQL API, we recommend you remove the deprecated schema from your GraphQL
API calls as soon as possible to avoid experiencing breaking changes.
The deprecation message provides an alternative for the deprecated schema item,
if applicable.

Deprecation example
The following fields are deprecated in different minor releases, but both
removed in GitLab 14.0:



Field deprecated in
Reason




12.7
GitLab traditionally has 12 minor releases per major release. To ensure the field is available for 6 more releases, it is removed in the 14.0 major release (and not 13.0).


13.6
The removal in 14.0 allows for 6 months of availability.




List of removed items
View the list of items removed in previous releases.

Available queries
The GraphQL API includes the following queries at the root level:



Query
Description




project
Project information and many of its associations, such as issues and merge requests.


group
Basic group information and epics.


user
Information about a particular user.


namespace
The namespace and the projects in it.


currentUser
Information about the signed-in user.


users
Information about a collection of users.


metaData
Metadata about GitLab and the GraphQL API.


snippets
Snippets visible to the signed-in user.



New associations and root level objects are regularly added.
See the GraphQL API Reference for up-to-date information.
Root-level queries are defined in
app/graphql/types/query_type.rb.

Multiplex queries
GitLab supports batching queries into a single request using
@apollo/client/link/batch-http. More
information about multiplexed queries is also available for
GraphQL Ruby, the
library GitLab uses on the backend.

Limits
The following limits apply to the GitLab GraphQL API.



Limit
Default




Max page size
100 records (nodes) per page. Applies to most connections in the API. Particular connections may have different max page size limits that are higher or lower.


Max query complexity

200 for unauthenticated requests and 250 for authenticated requests.


Request timeout
30 seconds.




Max query complexity
The GitLab GraphQL API scores the complexity of a query. Generally, larger
queries have a higher complexity score. This limit is designed to protect
the API from performing queries that could negatively impact its overall performance.
You can query the complexity score of a query
and the limit for the request.
If a query exceeds the complexity limit, an error message response is
returned.
In general, each field in a query adds 1 to the complexity score, although
this can be higher or lower for particular fields. Sometimes, adding
certain arguments may also increase the complexity of a query.
NOTE:
The complexity limits may be revised in future, and additionally, the complexity
of a query may be altered.

Resolve mutations detected as spam

Introduced in GitLab 13.11.

GraphQL mutations can be detected as spam. If a mutation is detected as spam and:


A CAPTCHA service is not configured, a
GraphQL top-level error is raised. For example:

{
  "errors": [
    {
      "message": "Request denied. Spam detected",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "spam": true
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null
    }
  }
}




A CAPTCHA service is configured, you receive a response with:


needsCaptchaResponse set to true.
The spamLogId and captchaSiteKey fields set.

For example:

{
  "errors": [
    {
      "message": "Request denied. Solve CAPTCHA challenge and retry",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "needsCaptchaResponse": true,
        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",
        "spamLogId": 67
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null,
    }
  }
}




Use the captchaSiteKey to obtain a CAPTCHA response value using the appropriate CAPTCHA API.
Only Google reCAPTCHA v2 is supported.


Resubmit the request with the X-GitLab-Captcha-Response and X-GitLab-Spam-Log-Id headers set.


NOTE:
The GitLab GraphiQL implementation doesn't permit passing of headers, so we must write
this as a cURL query. --data-binary is used to properly handle escaped double quotes
in the JSON-embedded query.

export CAPTCHA_RESPONSE="<CAPTCHA response obtained from CAPTCHA service>"
export SPAM_LOG_ID="<spam_log_id obtained from initial REST response>"
curl --header "Authorization: Bearer $PRIVATE_TOKEN" --header "Content-Type: application/json" --header "X-GitLab-Captcha-Response: $CAPTCHA_RESPONSE" --header "X-GitLab-Spam-Log-Id: $SPAM_LOG_ID" --request POST --data-binary '{"query": "mutation {createSnippet(input: {title: \"Title\" visibilityLevel: public blobActions: [ { action: create filePath: \"BlobPath\" content: \"BlobContent\" } ] }) { snippet { id title } errors }}"}' "https://gitlab.example.com/api/graphql"










Help

Help









Help

Help






Help

Help


Help
Help






GraphQL API (FREE)


Enabled and made generally available in GitLab 12.1. Feature flag graphql removed.

GraphQL is a query language for APIs. You can use it to
request the exact data you need, and therefore limit the number of requests you need.
GraphQL data is arranged in types, so your client can use
client-side GraphQL libraries
to consume the API and avoid manual parsing.
There are no fixed endpoints and no data model, so you can add
to the API without creating breaking changes.
This enables us to have a versionless API.

Vision
We want the GraphQL API to be the primary means of interacting
programmatically with GitLab. To achieve this, it needs full coverage - anything
possible in the REST API should also be possible in the GraphQL API.
To help us meet this vision, the frontend should use GraphQL in preference to
the REST API for new features.
There are no plans to deprecate the REST API. To reduce the technical burden of
supporting two APIs in parallel, they should share implementations as much as
possible.

Work with GraphQL
If you're new to the GitLab GraphQL API, see Get started with GitLab GraphQL API.
You can view the available resources in the GraphQL API reference.
The reference is automatically generated from the GitLab GraphQL schema and
written to a Markdown file.
The GitLab GraphQL API endpoint is located at /api/graphql.

GraphiQL
Explore the GraphQL API using the interactive GraphiQL explorer,
or on your self-managed GitLab instance on
https://<your-gitlab-site.com>/-/graphql-explorer.
For more information, see GraphiQL.

View GraphQL examples
You can work with sample queries that pull data from public projects on GitLab.com:

Create an audit report
Identify issue boards
Query users
Use custom emojis

The get started page includes different methods to customize GraphQL queries.

Breaking changes
The GitLab GraphQL API is versionless and changes to the API are primarily backward-compatible.
However, GitLab sometimes changes the GraphQL API in a way that is not backward-compatible. These changes are considered breaking changes, and
can include removing or renaming fields, arguments, or other parts of the schema.
When creating a breaking change, GitLab follows a deprecation and removal process.
Learn more about breaking changes.
Fields behind a feature flag and disabled by default do not follow the deprecation and removal process, and can be removed at any time without notice.
To avoid having a breaking change affect your integrations, you should
familiarize yourself with the deprecation and removal process.
WARNING:
GitLab makes all attempts to follow the deprecation and removal process.
On rare occasions, GitLab might make immediate breaking changes to the GraphQL
API to patch critical security or performance concerns if the deprecation
process would pose significant risk.

Deprecation and removal process
The deprecation and removal process for the GitLab GraphQL API aligns with the wider GitLab
deprecation process.
Parts of the schema marked for removal from the GitLab GraphQL API are first
deprecated
but still available for at least six releases. They are then removed
entirely during the next XX.0 major release.
Items are marked as deprecated in:

The schema.
The GraphQL API reference.
The deprecation feature removal schedule, which is linked from release posts.
Introspection queries of the GraphQL API.

NOTE:
If you use the GraphQL API, we recommend you remove the deprecated schema from your GraphQL
API calls as soon as possible to avoid experiencing breaking changes.
The deprecation message provides an alternative for the deprecated schema item,
if applicable.

Deprecation example
The following fields are deprecated in different minor releases, but both
removed in GitLab 14.0:



Field deprecated in
Reason




12.7
GitLab traditionally has 12 minor releases per major release. To ensure the field is available for 6 more releases, it is removed in the 14.0 major release (and not 13.0).


13.6
The removal in 14.0 allows for 6 months of availability.




List of removed items
View the list of items removed in previous releases.

Available queries
The GraphQL API includes the following queries at the root level:



Query
Description




project
Project information and many of its associations, such as issues and merge requests.


group
Basic group information and epics.


user
Information about a particular user.


namespace
The namespace and the projects in it.


currentUser
Information about the signed-in user.


users
Information about a collection of users.


metaData
Metadata about GitLab and the GraphQL API.


snippets
Snippets visible to the signed-in user.



New associations and root level objects are regularly added.
See the GraphQL API Reference for up-to-date information.
Root-level queries are defined in
app/graphql/types/query_type.rb.

Multiplex queries
GitLab supports batching queries into a single request using
@apollo/client/link/batch-http. More
information about multiplexed queries is also available for
GraphQL Ruby, the
library GitLab uses on the backend.

Limits
The following limits apply to the GitLab GraphQL API.



Limit
Default




Max page size
100 records (nodes) per page. Applies to most connections in the API. Particular connections may have different max page size limits that are higher or lower.


Max query complexity

200 for unauthenticated requests and 250 for authenticated requests.


Request timeout
30 seconds.




Max query complexity
The GitLab GraphQL API scores the complexity of a query. Generally, larger
queries have a higher complexity score. This limit is designed to protect
the API from performing queries that could negatively impact its overall performance.
You can query the complexity score of a query
and the limit for the request.
If a query exceeds the complexity limit, an error message response is
returned.
In general, each field in a query adds 1 to the complexity score, although
this can be higher or lower for particular fields. Sometimes, adding
certain arguments may also increase the complexity of a query.
NOTE:
The complexity limits may be revised in future, and additionally, the complexity
of a query may be altered.

Resolve mutations detected as spam

Introduced in GitLab 13.11.

GraphQL mutations can be detected as spam. If a mutation is detected as spam and:


A CAPTCHA service is not configured, a
GraphQL top-level error is raised. For example:

{
  "errors": [
    {
      "message": "Request denied. Spam detected",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "spam": true
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null
    }
  }
}




A CAPTCHA service is configured, you receive a response with:


needsCaptchaResponse set to true.
The spamLogId and captchaSiteKey fields set.

For example:

{
  "errors": [
    {
      "message": "Request denied. Solve CAPTCHA challenge and retry",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "needsCaptchaResponse": true,
        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",
        "spamLogId": 67
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null,
    }
  }
}




Use the captchaSiteKey to obtain a CAPTCHA response value using the appropriate CAPTCHA API.
Only Google reCAPTCHA v2 is supported.


Resubmit the request with the X-GitLab-Captcha-Response and X-GitLab-Spam-Log-Id headers set.


NOTE:
The GitLab GraphiQL implementation doesn't permit passing of headers, so we must write
this as a cURL query. --data-binary is used to properly handle escaped double quotes
in the JSON-embedded query.

export CAPTCHA_RESPONSE="<CAPTCHA response obtained from CAPTCHA service>"
export SPAM_LOG_ID="<spam_log_id obtained from initial REST response>"
curl --header "Authorization: Bearer $PRIVATE_TOKEN" --header "Content-Type: application/json" --header "X-GitLab-Captcha-Response: $CAPTCHA_RESPONSE" --header "X-GitLab-Spam-Log-Id: $SPAM_LOG_ID" --request POST --data-binary '{"query": "mutation {createSnippet(input: {title: \"Title\" visibilityLevel: public blobActions: [ { action: create filePath: \"BlobPath\" content: \"BlobContent\" } ] }) { snippet { id title } errors }}"}' "https://gitlab.example.com/api/graphql"







GraphQL API (FREE)


Enabled and made generally available in GitLab 12.1. Feature flag graphql removed.

GraphQL is a query language for APIs. You can use it to
request the exact data you need, and therefore limit the number of requests you need.
GraphQL data is arranged in types, so your client can use
client-side GraphQL libraries
to consume the API and avoid manual parsing.
There are no fixed endpoints and no data model, so you can add
to the API without creating breaking changes.
This enables us to have a versionless API.

Vision
We want the GraphQL API to be the primary means of interacting
programmatically with GitLab. To achieve this, it needs full coverage - anything
possible in the REST API should also be possible in the GraphQL API.
To help us meet this vision, the frontend should use GraphQL in preference to
the REST API for new features.
There are no plans to deprecate the REST API. To reduce the technical burden of
supporting two APIs in parallel, they should share implementations as much as
possible.

Work with GraphQL
If you're new to the GitLab GraphQL API, see Get started with GitLab GraphQL API.
You can view the available resources in the GraphQL API reference.
The reference is automatically generated from the GitLab GraphQL schema and
written to a Markdown file.
The GitLab GraphQL API endpoint is located at /api/graphql.

GraphiQL
Explore the GraphQL API using the interactive GraphiQL explorer,
or on your self-managed GitLab instance on
https://<your-gitlab-site.com>/-/graphql-explorer.
For more information, see GraphiQL.

View GraphQL examples
You can work with sample queries that pull data from public projects on GitLab.com:

Create an audit report
Identify issue boards
Query users
Use custom emojis

The get started page includes different methods to customize GraphQL queries.

Breaking changes
The GitLab GraphQL API is versionless and changes to the API are primarily backward-compatible.
However, GitLab sometimes changes the GraphQL API in a way that is not backward-compatible. These changes are considered breaking changes, and
can include removing or renaming fields, arguments, or other parts of the schema.
When creating a breaking change, GitLab follows a deprecation and removal process.
Learn more about breaking changes.
Fields behind a feature flag and disabled by default do not follow the deprecation and removal process, and can be removed at any time without notice.
To avoid having a breaking change affect your integrations, you should
familiarize yourself with the deprecation and removal process.
WARNING:
GitLab makes all attempts to follow the deprecation and removal process.
On rare occasions, GitLab might make immediate breaking changes to the GraphQL
API to patch critical security or performance concerns if the deprecation
process would pose significant risk.

Deprecation and removal process
The deprecation and removal process for the GitLab GraphQL API aligns with the wider GitLab
deprecation process.
Parts of the schema marked for removal from the GitLab GraphQL API are first
deprecated
but still available for at least six releases. They are then removed
entirely during the next XX.0 major release.
Items are marked as deprecated in:

The schema.
The GraphQL API reference.
The deprecation feature removal schedule, which is linked from release posts.
Introspection queries of the GraphQL API.

NOTE:
If you use the GraphQL API, we recommend you remove the deprecated schema from your GraphQL
API calls as soon as possible to avoid experiencing breaking changes.
The deprecation message provides an alternative for the deprecated schema item,
if applicable.

Deprecation example
The following fields are deprecated in different minor releases, but both
removed in GitLab 14.0:



Field deprecated in
Reason




12.7
GitLab traditionally has 12 minor releases per major release. To ensure the field is available for 6 more releases, it is removed in the 14.0 major release (and not 13.0).


13.6
The removal in 14.0 allows for 6 months of availability.




List of removed items
View the list of items removed in previous releases.

Available queries
The GraphQL API includes the following queries at the root level:



Query
Description




project
Project information and many of its associations, such as issues and merge requests.


group
Basic group information and epics.


user
Information about a particular user.


namespace
The namespace and the projects in it.


currentUser
Information about the signed-in user.


users
Information about a collection of users.


metaData
Metadata about GitLab and the GraphQL API.


snippets
Snippets visible to the signed-in user.



New associations and root level objects are regularly added.
See the GraphQL API Reference for up-to-date information.
Root-level queries are defined in
app/graphql/types/query_type.rb.

Multiplex queries
GitLab supports batching queries into a single request using
@apollo/client/link/batch-http. More
information about multiplexed queries is also available for
GraphQL Ruby, the
library GitLab uses on the backend.

Limits
The following limits apply to the GitLab GraphQL API.



Limit
Default




Max page size
100 records (nodes) per page. Applies to most connections in the API. Particular connections may have different max page size limits that are higher or lower.


Max query complexity

200 for unauthenticated requests and 250 for authenticated requests.


Request timeout
30 seconds.




Max query complexity
The GitLab GraphQL API scores the complexity of a query. Generally, larger
queries have a higher complexity score. This limit is designed to protect
the API from performing queries that could negatively impact its overall performance.
You can query the complexity score of a query
and the limit for the request.
If a query exceeds the complexity limit, an error message response is
returned.
In general, each field in a query adds 1 to the complexity score, although
this can be higher or lower for particular fields. Sometimes, adding
certain arguments may also increase the complexity of a query.
NOTE:
The complexity limits may be revised in future, and additionally, the complexity
of a query may be altered.

Resolve mutations detected as spam

Introduced in GitLab 13.11.

GraphQL mutations can be detected as spam. If a mutation is detected as spam and:


A CAPTCHA service is not configured, a
GraphQL top-level error is raised. For example:

{
  "errors": [
    {
      "message": "Request denied. Spam detected",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "spam": true
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null
    }
  }
}




A CAPTCHA service is configured, you receive a response with:


needsCaptchaResponse set to true.
The spamLogId and captchaSiteKey fields set.

For example:

{
  "errors": [
    {
      "message": "Request denied. Solve CAPTCHA challenge and retry",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "needsCaptchaResponse": true,
        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",
        "spamLogId": 67
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null,
    }
  }
}




Use the captchaSiteKey to obtain a CAPTCHA response value using the appropriate CAPTCHA API.
Only Google reCAPTCHA v2 is supported.


Resubmit the request with the X-GitLab-Captcha-Response and X-GitLab-Spam-Log-Id headers set.


NOTE:
The GitLab GraphiQL implementation doesn't permit passing of headers, so we must write
this as a cURL query. --data-binary is used to properly handle escaped double quotes
in the JSON-embedded query.

export CAPTCHA_RESPONSE="<CAPTCHA response obtained from CAPTCHA service>"
export SPAM_LOG_ID="<spam_log_id obtained from initial REST response>"
curl --header "Authorization: Bearer $PRIVATE_TOKEN" --header "Content-Type: application/json" --header "X-GitLab-Captcha-Response: $CAPTCHA_RESPONSE" --header "X-GitLab-Spam-Log-Id: $SPAM_LOG_ID" --request POST --data-binary '{"query": "mutation {createSnippet(input: {title: \"Title\" visibilityLevel: public blobActions: [ { action: create filePath: \"BlobPath\" content: \"BlobContent\" } ] }) { snippet { id title } errors }}"}' "https://gitlab.example.com/api/graphql"


Enabled and made generally available in GitLab 12.1. Feature flag graphql removed.GraphQL is a query language for APIs. You can use it to
request the exact data you need, and therefore limit the number of requests you need.GraphQL data is arranged in types, so your client can use
client-side GraphQL libraries
to consume the API and avoid manual parsing.There are no fixed endpoints and no data model, so you can add
to the API without creating breaking changes.
This enables us to have a versionless API.We want the GraphQL API to be the primary means of interacting
programmatically with GitLab. To achieve this, it needs full coverage - anything
possible in the REST API should also be possible in the GraphQL API.To help us meet this vision, the frontend should use GraphQL in preference to
the REST API for new features.There are no plans to deprecate the REST API. To reduce the technical burden of
supporting two APIs in parallel, they should share implementations as much as
possible.If you're new to the GitLab GraphQL API, see Get started with GitLab GraphQL API.You can view the available resources in the GraphQL API reference.
The reference is automatically generated from the GitLab GraphQL schema and
written to a Markdown file.The GitLab GraphQL API endpoint is located at /api/graphql.Explore the GraphQL API using the interactive GraphiQL explorer,
or on your self-managed GitLab instance on
https://<your-gitlab-site.com>/-/graphql-explorer.For more information, see GraphiQL.You can work with sample queries that pull data from public projects on GitLab.com:Create an audit reportIdentify issue boardsQuery usersUse custom emojisThe get started page includes different methods to customize GraphQL queries.The GitLab GraphQL API is versionless and changes to the API are primarily backward-compatible.However, GitLab sometimes changes the GraphQL API in a way that is not backward-compatible. These changes are considered breaking changes, and
can include removing or renaming fields, arguments, or other parts of the schema.
When creating a breaking change, GitLab follows a deprecation and removal process.Learn more about breaking changes.Fields behind a feature flag and disabled by default do not follow the deprecation and removal process, and can be removed at any time without notice.To avoid having a breaking change affect your integrations, you should
familiarize yourself with the deprecation and removal process.WARNING:
GitLab makes all attempts to follow the deprecation and removal process.
On rare occasions, GitLab might make immediate breaking changes to the GraphQL
API to patch critical security or performance concerns if the deprecation
process would pose significant risk.The deprecation and removal process for the GitLab GraphQL API aligns with the wider GitLab
deprecation process.Parts of the schema marked for removal from the GitLab GraphQL API are first
deprecated
but still available for at least six releases. They are then removed
entirely during the next XX.0 major release.Items are marked as deprecated in:The schema.The GraphQL API reference.The deprecation feature removal schedule, which is linked from release posts.Introspection queries of the GraphQL API.NOTE:
If you use the GraphQL API, we recommend you remove the deprecated schema from your GraphQL
API calls as soon as possible to avoid experiencing breaking changes.The deprecation message provides an alternative for the deprecated schema item,
if applicable.The following fields are deprecated in different minor releases, but both
removed in GitLab 14.0:View the list of items removed in previous releases.The GraphQL API includes the following queries at the root level:New associations and root level objects are regularly added.
See the GraphQL API Reference for up-to-date information.Root-level queries are defined in
app/graphql/types/query_type.rb.GitLab supports batching queries into a single request using
@apollo/client/link/batch-http. More
information about multiplexed queries is also available for
GraphQL Ruby, the
library GitLab uses on the backend.The following limits apply to the GitLab GraphQL API.The GitLab GraphQL API scores the complexity of a query. Generally, larger
queries have a higher complexity score. This limit is designed to protect
the API from performing queries that could negatively impact its overall performance.You can query the complexity score of a query
and the limit for the request.If a query exceeds the complexity limit, an error message response is
returned.In general, each field in a query adds 1 to the complexity score, although
this can be higher or lower for particular fields. Sometimes, adding
certain arguments may also increase the complexity of a query.NOTE:
The complexity limits may be revised in future, and additionally, the complexity
of a query may be altered.Introduced in GitLab 13.11.GraphQL mutations can be detected as spam. If a mutation is detected as spam and:
A CAPTCHA service is not configured, a
GraphQL top-level error is raised. For example:

{
  "errors": [
    {
      "message": "Request denied. Spam detected",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "spam": true
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null
    }
  }
}


A CAPTCHA service is not configured, a
GraphQL top-level error is raised. For example:
{
  "errors": [
    {
      "message": "Request denied. Spam detected",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "spam": true
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null
    }
  }
}

{{  "errors": [  "errors": [    {    {      "message": "Request denied. Spam detected",      "message": "Request denied. Spam detected",      "locations": [ { "line": 6, "column": 7 } ],      "locations": [ { "line": 6, "column": 7 } ],      "path": [ "updateSnippet" ],      "path": [ "updateSnippet" ],      "extensions": {      "extensions": {        "spam": true        "spam": true      }      }    }    }  ],  ],  "data": {  "data": {    "updateSnippet": {    "updateSnippet": {      "snippet": null      "snippet": null    }    }  }  }}}
A CAPTCHA service is configured, you receive a response with:


needsCaptchaResponse set to true.
The spamLogId and captchaSiteKey fields set.

For example:

{
  "errors": [
    {
      "message": "Request denied. Solve CAPTCHA challenge and retry",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "needsCaptchaResponse": true,
        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",
        "spamLogId": 67
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null,
    }
  }
}


A CAPTCHA service is configured, you receive a response with:
needsCaptchaResponse set to true.The spamLogId and captchaSiteKey fields set.For example:
{
  "errors": [
    {
      "message": "Request denied. Solve CAPTCHA challenge and retry",
      "locations": [ { "line": 6, "column": 7 } ],
      "path": [ "updateSnippet" ],
      "extensions": {
        "needsCaptchaResponse": true,
        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",
        "spamLogId": 67
      }
    }
  ],
  "data": {
    "updateSnippet": {
      "snippet": null,
    }
  }
}

{{  "errors": [  "errors": [    {    {      "message": "Request denied. Solve CAPTCHA challenge and retry",      "message": "Request denied. Solve CAPTCHA challenge and retry",      "locations": [ { "line": 6, "column": 7 } ],      "locations": [ { "line": 6, "column": 7 } ],      "path": [ "updateSnippet" ],      "path": [ "updateSnippet" ],      "extensions": {      "extensions": {        "needsCaptchaResponse": true,        "needsCaptchaResponse": true,        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",        "captchaSiteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",        "spamLogId": 67        "spamLogId": 67      }      }    }    }  ],  ],  "data": {  "data": {    "updateSnippet": {    "updateSnippet": {      "snippet": null,      "snippet": null,    }    }  }  }}}
Use the captchaSiteKey to obtain a CAPTCHA response value using the appropriate CAPTCHA API.
Only Google reCAPTCHA v2 is supported.
Use the captchaSiteKey to obtain a CAPTCHA response value using the appropriate CAPTCHA API.
Only Google reCAPTCHA v2 is supported.
Resubmit the request with the X-GitLab-Captcha-Response and X-GitLab-Spam-Log-Id headers set.
Resubmit the request with the X-GitLab-Captcha-Response and X-GitLab-Spam-Log-Id headers set.NOTE:
The GitLab GraphiQL implementation doesn't permit passing of headers, so we must write
this as a cURL query. --data-binary is used to properly handle escaped double quotes
in the JSON-embedded query.
export CAPTCHA_RESPONSE="<CAPTCHA response obtained from CAPTCHA service>"
export SPAM_LOG_ID="<spam_log_id obtained from initial REST response>"
curl --header "Authorization: Bearer $PRIVATE_TOKEN" --header "Content-Type: application/json" --header "X-GitLab-Captcha-Response: $CAPTCHA_RESPONSE" --header "X-GitLab-Spam-Log-Id: $SPAM_LOG_ID" --request POST --data-binary '{"query": "mutation {createSnippet(input: {title: \"Title\" visibilityLevel: public blobActions: [ { action: create filePath: \"BlobPath\" content: \"BlobContent\" } ] }) { snippet { id title } errors }}"}' "https://gitlab.example.com/api/graphql"

export CAPTCHA_RESPONSE="<CAPTCHA response obtained from CAPTCHA service>"export CAPTCHA_RESPONSE="<CAPTCHA response obtained from CAPTCHA service>"export SPAM_LOG_ID="<spam_log_id obtained from initial REST response>"export SPAM_LOG_ID="<spam_log_id obtained from initial REST response>"curl --header "Authorization: Bearer $PRIVATE_TOKEN" --header "Content-Type: application/json" --header "X-GitLab-Captcha-Response: $CAPTCHA_RESPONSE" --header "X-GitLab-Spam-Log-Id: $SPAM_LOG_ID" --request POST --data-binary '{"query": "mutation {createSnippet(input: {title: \"Title\" visibilityLevel: public blobActions: [ { action: create filePath: \"BlobPath\" content: \"BlobContent\" } ] }) { snippet { id title } errors }}"}' "https://gitlab.example.com/api/graphql"--header"Authorization: Bearer $PRIVATE_TOKEN"--header"Content-Type: application/json"--header"X-GitLab-Captcha-Response: $CAPTCHA_RESPONSE"--header"X-GitLab-Spam-Log-Id: $SPAM_LOG_ID"--request--data-binary'{"query": "mutation {createSnippet(input: {title: \"Title\" visibilityLevel: public blobActions: [ { action: create filePath: \"BlobPath\" content: \"BlobContent\" } ] }) { snippet { id title } errors }}"}'"https://gitlab.example.com/api/graphql"





