



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













GitLab integrations (FREE)

GitLab can be integrated with external services for enhanced functionality.

Issue trackers
You can use an external issue tracker at the same time as the GitLab
issue tracker, or use only the external issue tracker.

Authentication sources
GitLab can be configured to authenticate access requests with the following authentication sources:

Enable the Auth0 OmniAuth provider.
Enable sign in with Bitbucket accounts.
Configure GitLab to sign in using CAS.
Integrate with Kerberos.
Enable sign in via LDAP.
Enable OAuth2 provider application creation.
Use OmniAuth to enable sign in through Twitter, GitHub, GitLab.com, Google,
Bitbucket, Facebook, SAML, Crowd, Azure, or Authentiq ID.
Use GitLab as an OpenID Connect identity provider.
Authenticate to Vault through GitLab OpenID Connect.
Configure GitLab as a SAML 2.0 Service Provider.


Security enhancements
GitLab can be integrated with the following external services to enhance security:


Akismet helps reduce spam.
Google reCAPTCHA helps verify new users.

GitLab also provides features to improve the security of your own application. For more details see GitLab Secure.

Security partners
GitLab has integrated with several security partners. For more information, see
Security partners integration.

Continuous integration
GitLab can be integrated with the following external services for continuous integration:


Jenkins CI.

Datadog, to monitor for CI/CD job failures and performance issues.


Feature enhancements
GitLab can be integrated with the following enhancements:

Add GitLab actions to Gmail actions buttons.
Configure PlantUML
or Kroki to use diagrams in AsciiDoc and Markdown documents.
Attach merge requests to Trello cards.
Enable integrated code intelligence powered by Sourcegraph.
Add Elasticsearch for Advanced Search.


Integrations
Integration with services such as Campfire, Flowdock, Jira, Pivotal Tracker, and Slack are available as Integrations.

Troubleshooting

SSL certificate errors
When trying to integrate GitLab with services using self-signed certificates,
SSL certificate errors can occur in different parts of the application. Sidekiq
is a common culprit.
There are two approaches you can take to solve this:

Add the root certificate to the trusted chain of the OS.
If using Omnibus, you can add the certificate to the GitLab trusted certificates.

OS main trusted chain
This resource
has all the information you need to add a certificate to the main trusted chain.
This answer
at Super User also has relevant information.
Omnibus Trusted Chain
Install the self signed certificate or custom certificate authorities
in to Omnibus GitLab.
It is enough to concatenate the certificate to the main trusted certificate
however it may be overwritten during upgrades:

cat jira.pem >> /opt/gitlab/embedded/ssl/certs/cacert.pem


After that restart GitLab with:

sudo gitlab-ctl restart



Search Sidekiq logs in Kibana
To locate a specific integration in Kibana, use the following KQL search string:

`json.integration_class.keyword : "Integrations::Jira" and json.project_path : "path/to/project"`


You can find information in json.exception.backtrace, json.exception.class, json.exception.message, and json.message.











Help

Help













GitLab integrations (FREE)

GitLab can be integrated with external services for enhanced functionality.

Issue trackers
You can use an external issue tracker at the same time as the GitLab
issue tracker, or use only the external issue tracker.

Authentication sources
GitLab can be configured to authenticate access requests with the following authentication sources:

Enable the Auth0 OmniAuth provider.
Enable sign in with Bitbucket accounts.
Configure GitLab to sign in using CAS.
Integrate with Kerberos.
Enable sign in via LDAP.
Enable OAuth2 provider application creation.
Use OmniAuth to enable sign in through Twitter, GitHub, GitLab.com, Google,
Bitbucket, Facebook, SAML, Crowd, Azure, or Authentiq ID.
Use GitLab as an OpenID Connect identity provider.
Authenticate to Vault through GitLab OpenID Connect.
Configure GitLab as a SAML 2.0 Service Provider.


Security enhancements
GitLab can be integrated with the following external services to enhance security:


Akismet helps reduce spam.
Google reCAPTCHA helps verify new users.

GitLab also provides features to improve the security of your own application. For more details see GitLab Secure.

Security partners
GitLab has integrated with several security partners. For more information, see
Security partners integration.

Continuous integration
GitLab can be integrated with the following external services for continuous integration:


Jenkins CI.

Datadog, to monitor for CI/CD job failures and performance issues.


Feature enhancements
GitLab can be integrated with the following enhancements:

Add GitLab actions to Gmail actions buttons.
Configure PlantUML
or Kroki to use diagrams in AsciiDoc and Markdown documents.
Attach merge requests to Trello cards.
Enable integrated code intelligence powered by Sourcegraph.
Add Elasticsearch for Advanced Search.


Integrations
Integration with services such as Campfire, Flowdock, Jira, Pivotal Tracker, and Slack are available as Integrations.

Troubleshooting

SSL certificate errors
When trying to integrate GitLab with services using self-signed certificates,
SSL certificate errors can occur in different parts of the application. Sidekiq
is a common culprit.
There are two approaches you can take to solve this:

Add the root certificate to the trusted chain of the OS.
If using Omnibus, you can add the certificate to the GitLab trusted certificates.

OS main trusted chain
This resource
has all the information you need to add a certificate to the main trusted chain.
This answer
at Super User also has relevant information.
Omnibus Trusted Chain
Install the self signed certificate or custom certificate authorities
in to Omnibus GitLab.
It is enough to concatenate the certificate to the main trusted certificate
however it may be overwritten during upgrades:

cat jira.pem >> /opt/gitlab/embedded/ssl/certs/cacert.pem


After that restart GitLab with:

sudo gitlab-ctl restart



Search Sidekiq logs in Kibana
To locate a specific integration in Kibana, use the following KQL search string:

`json.integration_class.keyword : "Integrations::Jira" and json.project_path : "path/to/project"`


You can find information in json.exception.backtrace, json.exception.class, json.exception.message, and json.message.








Help

Help









Help

Help






Help

Help


Help
Help






GitLab integrations (FREE)

GitLab can be integrated with external services for enhanced functionality.

Issue trackers
You can use an external issue tracker at the same time as the GitLab
issue tracker, or use only the external issue tracker.

Authentication sources
GitLab can be configured to authenticate access requests with the following authentication sources:

Enable the Auth0 OmniAuth provider.
Enable sign in with Bitbucket accounts.
Configure GitLab to sign in using CAS.
Integrate with Kerberos.
Enable sign in via LDAP.
Enable OAuth2 provider application creation.
Use OmniAuth to enable sign in through Twitter, GitHub, GitLab.com, Google,
Bitbucket, Facebook, SAML, Crowd, Azure, or Authentiq ID.
Use GitLab as an OpenID Connect identity provider.
Authenticate to Vault through GitLab OpenID Connect.
Configure GitLab as a SAML 2.0 Service Provider.


Security enhancements
GitLab can be integrated with the following external services to enhance security:


Akismet helps reduce spam.
Google reCAPTCHA helps verify new users.

GitLab also provides features to improve the security of your own application. For more details see GitLab Secure.

Security partners
GitLab has integrated with several security partners. For more information, see
Security partners integration.

Continuous integration
GitLab can be integrated with the following external services for continuous integration:


Jenkins CI.

Datadog, to monitor for CI/CD job failures and performance issues.


Feature enhancements
GitLab can be integrated with the following enhancements:

Add GitLab actions to Gmail actions buttons.
Configure PlantUML
or Kroki to use diagrams in AsciiDoc and Markdown documents.
Attach merge requests to Trello cards.
Enable integrated code intelligence powered by Sourcegraph.
Add Elasticsearch for Advanced Search.


Integrations
Integration with services such as Campfire, Flowdock, Jira, Pivotal Tracker, and Slack are available as Integrations.

Troubleshooting

SSL certificate errors
When trying to integrate GitLab with services using self-signed certificates,
SSL certificate errors can occur in different parts of the application. Sidekiq
is a common culprit.
There are two approaches you can take to solve this:

Add the root certificate to the trusted chain of the OS.
If using Omnibus, you can add the certificate to the GitLab trusted certificates.

OS main trusted chain
This resource
has all the information you need to add a certificate to the main trusted chain.
This answer
at Super User also has relevant information.
Omnibus Trusted Chain
Install the self signed certificate or custom certificate authorities
in to Omnibus GitLab.
It is enough to concatenate the certificate to the main trusted certificate
however it may be overwritten during upgrades:

cat jira.pem >> /opt/gitlab/embedded/ssl/certs/cacert.pem


After that restart GitLab with:

sudo gitlab-ctl restart



Search Sidekiq logs in Kibana
To locate a specific integration in Kibana, use the following KQL search string:

`json.integration_class.keyword : "Integrations::Jira" and json.project_path : "path/to/project"`


You can find information in json.exception.backtrace, json.exception.class, json.exception.message, and json.message.





GitLab integrations (FREE)

GitLab can be integrated with external services for enhanced functionality.

Issue trackers
You can use an external issue tracker at the same time as the GitLab
issue tracker, or use only the external issue tracker.

Authentication sources
GitLab can be configured to authenticate access requests with the following authentication sources:

Enable the Auth0 OmniAuth provider.
Enable sign in with Bitbucket accounts.
Configure GitLab to sign in using CAS.
Integrate with Kerberos.
Enable sign in via LDAP.
Enable OAuth2 provider application creation.
Use OmniAuth to enable sign in through Twitter, GitHub, GitLab.com, Google,
Bitbucket, Facebook, SAML, Crowd, Azure, or Authentiq ID.
Use GitLab as an OpenID Connect identity provider.
Authenticate to Vault through GitLab OpenID Connect.
Configure GitLab as a SAML 2.0 Service Provider.


Security enhancements
GitLab can be integrated with the following external services to enhance security:


Akismet helps reduce spam.
Google reCAPTCHA helps verify new users.

GitLab also provides features to improve the security of your own application. For more details see GitLab Secure.

Security partners
GitLab has integrated with several security partners. For more information, see
Security partners integration.

Continuous integration
GitLab can be integrated with the following external services for continuous integration:


Jenkins CI.

Datadog, to monitor for CI/CD job failures and performance issues.


Feature enhancements
GitLab can be integrated with the following enhancements:

Add GitLab actions to Gmail actions buttons.
Configure PlantUML
or Kroki to use diagrams in AsciiDoc and Markdown documents.
Attach merge requests to Trello cards.
Enable integrated code intelligence powered by Sourcegraph.
Add Elasticsearch for Advanced Search.


Integrations
Integration with services such as Campfire, Flowdock, Jira, Pivotal Tracker, and Slack are available as Integrations.

Troubleshooting

SSL certificate errors
When trying to integrate GitLab with services using self-signed certificates,
SSL certificate errors can occur in different parts of the application. Sidekiq
is a common culprit.
There are two approaches you can take to solve this:

Add the root certificate to the trusted chain of the OS.
If using Omnibus, you can add the certificate to the GitLab trusted certificates.

OS main trusted chain
This resource
has all the information you need to add a certificate to the main trusted chain.
This answer
at Super User also has relevant information.
Omnibus Trusted Chain
Install the self signed certificate or custom certificate authorities
in to Omnibus GitLab.
It is enough to concatenate the certificate to the main trusted certificate
however it may be overwritten during upgrades:

cat jira.pem >> /opt/gitlab/embedded/ssl/certs/cacert.pem


After that restart GitLab with:

sudo gitlab-ctl restart



Search Sidekiq logs in Kibana
To locate a specific integration in Kibana, use the following KQL search string:

`json.integration_class.keyword : "Integrations::Jira" and json.project_path : "path/to/project"`


You can find information in json.exception.backtrace, json.exception.class, json.exception.message, and json.message.
GitLab can be integrated with external services for enhanced functionality.You can use an external issue tracker at the same time as the GitLab
issue tracker, or use only the external issue tracker.GitLab can be configured to authenticate access requests with the following authentication sources:Enable the Auth0 OmniAuth provider.Enable sign in with Bitbucket accounts.Configure GitLab to sign in using CAS.Integrate with Kerberos.Enable sign in via LDAP.Enable OAuth2 provider application creation.Use OmniAuth to enable sign in through Twitter, GitHub, GitLab.com, Google,
Bitbucket, Facebook, SAML, Crowd, Azure, or Authentiq ID.Use GitLab as an OpenID Connect identity provider.Authenticate to Vault through GitLab OpenID Connect.Configure GitLab as a SAML 2.0 Service Provider.GitLab can be integrated with the following external services to enhance security:
Akismet helps reduce spam.Google reCAPTCHA helps verify new users.GitLab also provides features to improve the security of your own application. For more details see GitLab Secure.GitLab has integrated with several security partners. For more information, see
Security partners integration.GitLab can be integrated with the following external services for continuous integration:
Jenkins CI.
Datadog, to monitor for CI/CD job failures and performance issues.GitLab can be integrated with the following enhancements:Add GitLab actions to Gmail actions buttons.Configure PlantUML
or Kroki to use diagrams in AsciiDoc and Markdown documents.Attach merge requests to Trello cards.Enable integrated code intelligence powered by Sourcegraph.Add Elasticsearch for Advanced Search.Integration with services such as Campfire, Flowdock, Jira, Pivotal Tracker, and Slack are available as Integrations.When trying to integrate GitLab with services using self-signed certificates,
SSL certificate errors can occur in different parts of the application. Sidekiq
is a common culprit.There are two approaches you can take to solve this:Add the root certificate to the trusted chain of the OS.If using Omnibus, you can add the certificate to the GitLab trusted certificates.OS main trusted chainThis resource
has all the information you need to add a certificate to the main trusted chain.This answer
at Super User also has relevant information.Omnibus Trusted ChainInstall the self signed certificate or custom certificate authorities
in to Omnibus GitLab.It is enough to concatenate the certificate to the main trusted certificate
however it may be overwritten during upgrades:
cat jira.pem >> /opt/gitlab/embedded/ssl/certs/cacert.pem

cat jira.pem >> /opt/gitlab/embedded/ssl/certs/cacert.pemcat >>After that restart GitLab with:
sudo gitlab-ctl restart

sudo gitlab-ctl restartsudo To locate a specific integration in Kibana, use the following KQL search string:
`json.integration_class.keyword : "Integrations::Jira" and json.project_path : "path/to/project"`

`json.integration_class.keyword : "Integrations::Jira" and json.project_path : "path/to/project"`You can find information in json.exception.backtrace, json.exception.class, json.exception.message, and json.message.





