



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













GitLab Documentation guidelines
The GitLab documentation is intended as the single source of truth (SSOT) for information about how to configure, use, and troubleshoot GitLab. The documentation contains use cases and usage instructions for every GitLab feature, organized by product area and subject. This includes topics and workflows that span multiple GitLab features and the use of GitLab with other applications.
In addition to this page, the following resources can help you craft and contribute to documentation:


Style Guide - What belongs in the docs, language guidelines, Markdown standards to follow, links, and more.

Topic type template - Learn about the different types of topics.

Documentation process.

Markdown Guide - A reference for all Markdown syntax supported by GitLab.

Site architecture - How https://docs.gitlab.com is built.

Documentation for feature flags - How to write and update documentation for GitLab features deployed behind feature flags.


Source files and rendered web locations
Documentation for GitLab, GitLab Runner, GitLab Operator, Omnibus GitLab, and Charts is published to https://docs.gitlab.com. Documentation for GitLab is also published within the application at /help on the domain of the GitLab instance.
At /help, only help for your current edition and version is included. Help for other versions is available at https://docs.gitlab.com/archives/.
The source of the documentation exists within the codebase of each GitLab application in the following repository locations:



Project
Path




GitLab
/doc


GitLab Runner
/docs


Omnibus GitLab
/doc


Charts
/doc


GitLab Operator
/doc



Documentation issues and merge requests are part of their respective repositories and all have the label Documentation.

Branch naming
The CI pipeline for the main GitLab project is configured to automatically
run only the jobs that match the type of contribution. If your contribution contains
only documentation changes, then only documentation-related jobs run, and
the pipeline completes much faster than a code contribution.
If you are submitting documentation-only changes to Omnibus or Charts,
the fast pipeline is not determined automatically. Instead, create branches for
docs-only merge requests using the following guide:



Branch name
Valid example




Starting with docs/

docs/update-api-issues


Starting with docs-

docs-update-api-issues


Ending in -docs

123-update-api-issues-docs




Contributing to docs
Contributions to GitLab docs are welcome from the entire GitLab community.
To ensure that the GitLab docs are current, there are special processes and responsibilities for all feature changes, that is development work that impacts the appearance, usage, or administration of a feature.
However, anyone can contribute documentation improvements that are not associated with a feature change. For example, adding a new document on how to accomplish a use case that's already possible with GitLab or with third-party tools and GitLab.

Markdown and styles
GitLab docs uses GitLab Kramdown
as its Markdown rendering engine. See the GitLab Markdown Guide for a complete Kramdown reference.
Adhere to the Documentation Style Guide. If a style standard is missing, you are welcome to suggest one via a merge request.

Folder structure and files
See the Folder structure page.

Metadata
To provide additional directives or useful information, we add metadata in YAML
format to the beginning of each product documentation page (YAML front matter).
All values are treated as strings and are only used for the
docs website.

Stage and group metadata
Each page should ideally have metadata related to the stage and group it
belongs to, as well as an information block as described below:


stage: The Stage
to which the majority of the page's content belongs.


group: The Group
to which the majority of the page's content belongs.


info: The following line, which provides direction to contributors regarding
how to contact the Technical Writer associated with the page's stage and
group:

To determine the technical writer assigned to the Stage/Group
associated with this page, see
https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments




For example:

---
stage: Example Stage
group: Example Group
info: To determine the technical writer assigned to the Stage/Group associated with this page, see https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments
---


If you need help determining the correct stage, read Ask for help.

Redirection metadata
The following metadata should be added when a page is moved to another location:


redirect_to: The relative path and filename (with an .md extension) of the
location to which visitors should be redirected for a moved page.
Learn more.

disqus_identifier: Identifier for Disqus commenting system. Used to keep
comments with a page that has been moved to a new URL.
Learn more.


Comments metadata
The docs website has comments (provided by Disqus)
enabled by default. In case you want to disable them (for example in index pages),
set it to false:

---
comments: false
---



Additional page metadata
Each page can have additional, optional metadata (set in the
default.html
Nanoc layout), which is displayed at the top of the page if defined.

Deprecated metadata
The type metadata parameter is deprecated but still exists in documentation
pages. You can safely remove the type metadata parameter and its values.

Batch updates for TW metadata
NOTE:
This task is an MVC, and requires significant manual preparation of the output.
While the task can be time consuming, it is still faster than doing the work
entirely manually.
It's important to keep the CODEOWNERS
file in the gitlab project up to date with the current Technical Writing team assignments.
This information is used in merge requests that contain documentation:

To populate the eligible approvers section.
By GitLab Bot to ping reviewers for community contributions.

GitLab cannot automatically associate the stage and group metadata in our documentation
pages with the technical writer assigned to that group, so we use a Rake task to
generate entries for the CODEOWNERS file. Declaring code owners for pages reduces
the number of times GitLab Bot pings the entire Technical Writing team.
The tw:codeowners Rake task, located in lib/tasks/gitlab/tw/codeowners.rake,
contains an array of groups and their assigned technical writer. This task:

Outputs a line for each doc with metadata that matches a group in lib/tasks/gitlab/tw/codeowners.rake.
Files not matching a group are skipped.
Adds the full path to the page, and the assigned technical writer.

To prepare an update to the CODEOWNERS file:


Update lib/tasks/gitlab/tw/codeowners.rake with the latest TW team assignments.
Make this update in a standalone merge request, as it runs a long pipeline and
requires backend maintainer review. Make sure this is merged before you update
CODEOWNERS in another merge request.


Run the task from the root directory of the gitlab repository, and save the output in a file:

bundle exec rake tw:codeowners > ~/Desktop/updates.md




Open the file you just created (~/Desktop/updates.md in this example), and prepare the output:

Find and replace ./ with /.
Sort the lines in alphabetical (ascending) order. If you use VS Code, you can
select everything, press F1, type sort, and select Sort lines (ascending, case insensitive.



Create a new branch for your CODEOWNERS updates.


Replace the documentation-related lines in the ^[Documentation Pages] section
with the output you prepared.
WARNING:
The documentation section is not the last section of the CODEOWNERS file. Don't
delete data that isn't ours!


Create a commit with the raw changes.


From the command line, run git diff master.


In the diff, look for directory-level assignments to manually restore to the
CODEOWNERS file. If all files in a single directory are assigned to the same
technical writer, we simplify these entries. Remove all the lines for the individual
files, and leave a single entry for the directory, for example: /doc/directory/ @tech.writer.


In the diff, look for changes that don't match your expectations:

New pages, or newly moved pages, show up as added lines.
Deleted pages, and pages that are now redirects, show up as deleted lines.
If you see an unusual number of changes to pages that all seem related,
check the metadata for the pages. A group might have been renamed and the Rake task
must be updated to match.



Create another commit with your manual changes, and create a second merge request
with your changes to the CODEOWNERS file. Assign it to a technical writing manager for review.



Move, rename, or delete a page
See redirects.

Merge requests for GitLab documentation
Before getting started, make sure you read the introductory section
"contributing to docs" above and the
documentation workflow.

Use the current merge request description template

Label the MR Documentation (can only be done by people with developer access, for example, GitLab team members)
Assign the correct milestone per note below (can only be done by people with developer access, for example, GitLab team members)

Documentation is merged if it is an improvement on existing content,
represents a good-faith effort to follow the template and style standards,
and is believed to be accurate.
Further needs for what would make the doc even better should be immediately addressed
in a follow-up merge request or issue.
If the release version you want to add the documentation to has already been
frozen or released, use the label ~"Pick into X.Y" to get it merged into
the correct release. Avoid picking into a past release as much as you can, as
it increases the work of the release managers.

GitLab /help

Every GitLab instance includes documentation at /help (https://gitlab.example.com/help)
that matches the version of the instance. For example, https://gitlab.com/help.
The documentation available online at https://docs.gitlab.com is deployed every
four hours from the default branch of GitLab, Omnibus, Runner, and Charts.
After a merge request that updates documentation is merged, it is available online
in 4 hours or less.
However, it's only available at /help on self-managed instances in the next released
version. The date an update is merged can impact which self-managed release the update
is present in.
For example:

A merge request in gitlab updates documentation. It has a milestone of 14.4,
with an expected release date of 2021-10-22.
It is merged on 2021-10-19 and available online the same day at https://docs.gitlab.com.
GitLab 14.4 is released on 2021-10-22, based on the gitlab codebase from 2021-10-18
(one day before the update was merged).
The change shows up in the 14.5 self-managed release, due to missing the release cutoff
for 14.4.

The exact cutoff date for each release is flexible, and can be sooner or later
than expected due to holidays, weekends or other events. In general, MRs merged
by the 17th should be present in the release on the 22nd, though it is not guaranteed.
If it is important that a documentation update is present in that month's release,
merge it as early as possible.

Linking to /help

When you're building a new feature, you may need to link to the documentation
from the GitLab application. This is normally done in files inside the
app/views/ directory, with the help of the help_page_path helper method.
The help_page_path contains the path to the document you want to link to,
with the following conventions:

It's relative to the doc/ directory in the GitLab repository.
It omits the .md extension.
It doesn't end with a forward slash (/).

The help text follows the Pajamas guidelines.

Linking to /help in HAML
Use the following special cases depending on the context, ensuring all link text
is inside _() so it can be translated:


Linking to a doc page. In its most basic form, the HAML code to generate a
link to the /help page is:

= link_to _('Learn more.'), help_page_path('user/permissions'), target: '_blank', rel: 'noopener noreferrer'




Linking to an anchor link. Use anchor as part of the help_page_path
method:

= link_to _('Learn more.'), help_page_path('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'




Using links inline of some text. First, define the link, and then use it. In
this example, link_start is the name of the variable that contains the
link:

- link_start = '<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe % { url: help_page_path('user/permissions') }
%p= _("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe % { link_start: link_start, link_end: '</a>'.html_safe }




Using a button link. Useful in places where text would be out of context with
the rest of the page layout:

= link_to _('Learn more.'), help_page_path('user/permissions'),  class: 'btn btn-info', target: '_blank', rel: 'noopener noreferrer'





Linking to /help in JavaScript
To link to the documentation from a JavaScript or a Vue component, use the helpPagePath function from help_page_helper.js:

import { helpPagePath } from '~/helpers/help_page_helper';

helpPagePath('user/permissions', { anchor: 'anchor-link' })
// evaluates to '/help/user/permissions#anchor-link' for GitLab.com


This is preferred over static paths, as the helper also works on instances installed under a relative URL.

Linking to /help in Ruby
To link to the documentation from within Ruby code, use the following code block as a guide, ensuring all link text is inside _() so it can
be translated:

docs_link = link_to _('Learn more.'), help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


In cases where you need to generate a link from outside of views/helpers, where the link_to and help_page_url methods are not available, use the following code block
as a guide where the methods are fully qualified:

docs_link = ActionController::Base.helpers.link_to _('Learn more.'), Rails.application.routes.url_helpers.help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


Do not use include ActionView::Helpers::UrlHelper just to make the link_to method available as you might see in some existing code. Read more in
issue 340567.

GitLab /help tests
Several RSpec tests
are run to ensure GitLab documentation renders and works correctly. In particular, that main docs landing page works correctly from /help.
For example, GitLab.com's /help.

Docs site architecture
For information on how we build and deploy https://docs.gitlab.com, see Docs site architecture.

Global navigation
See the Global navigation doc for information
on how the left-side navigation menu is built and updated.

Previewing the changes live
See how you can use review apps to preview your changes live.

Testing
For more information about documentation testing, see the Documentation testing
guide.

Danger Bot
GitLab uses Danger for some elements in
code review. For docs changes in merge requests, whenever a change to files under /doc
is made, Danger Bot leaves a comment with further instructions about the documentation
process. This is configured in the Dangerfile in the GitLab repository under
/danger/documentation/.

Automatic screenshot generator
You can now set up an automatic screenshot generator to take and compress screenshots with the
help of a configuration file known as screenshot generator.

Use the tool
To run the tool on an existing screenshot generator, take the following steps:

Set up the GitLab Development Kit (GDK).
Navigate to the subdirectory with your cloned GitLab repository, typically gdk/gitlab.
Make sure that your GDK database is fully migrated: bin/rake db:migrate RAILS_ENV=development.
Install pngquant, see the tool website for more information: pngquant

Run scripts/docs_screenshots.rb spec/docs_screenshots/<name_of_screenshot_generator>.rb <milestone-version>.
Identify the location of the screenshots, based on the gitlab/doc location defined by the it parameter in your script.
Commit the newly created screenshots.


Extending the tool
To add an additional screenshot generator, take the following steps:

Locate the spec/docs_screenshots directory.
Add a new file with a _docs.rb extension.
Be sure to include the following bits in the file:


require 'spec_helper'

RSpec.describe '<What I am taking screenshots of>', :js do
  include DocsScreenshotHelpers # Helper that enables the screenshots taking mechanism

  before do
    page.driver.browser.manage.window.resize_to(1366, 1024) # length and width of the page
  end



In addition, every it block must include the path where the screenshot is saved


 it 'user/packages/container_registry/img/project_image_repositories_list'



Full page screenshots
To take a full page screenshot simply visit the page and perform any expectation on real content (to have capybara wait till the page is ready and not take a white screenshot).

Element screenshot
To have the screenshot focuses few more steps are needed:


find the area: screenshot_area = find('#js-registry-policies')


scroll the area in focus: scroll_to screenshot_area


wait for the content: expect(screenshot_area).to have_content 'Expiration interval'


set the crop area: set_crop_data(screenshot_area, 20)


In particular, set_crop_data accepts as arguments: a DOM element and a
padding. The padding is added around the element, enlarging the screenshot area.

Live example
Please use spec/docs_screenshots/container_registry_docs.rb as a guide and as an example to create your own scripts.











Help

Help













GitLab Documentation guidelines
The GitLab documentation is intended as the single source of truth (SSOT) for information about how to configure, use, and troubleshoot GitLab. The documentation contains use cases and usage instructions for every GitLab feature, organized by product area and subject. This includes topics and workflows that span multiple GitLab features and the use of GitLab with other applications.
In addition to this page, the following resources can help you craft and contribute to documentation:


Style Guide - What belongs in the docs, language guidelines, Markdown standards to follow, links, and more.

Topic type template - Learn about the different types of topics.

Documentation process.

Markdown Guide - A reference for all Markdown syntax supported by GitLab.

Site architecture - How https://docs.gitlab.com is built.

Documentation for feature flags - How to write and update documentation for GitLab features deployed behind feature flags.


Source files and rendered web locations
Documentation for GitLab, GitLab Runner, GitLab Operator, Omnibus GitLab, and Charts is published to https://docs.gitlab.com. Documentation for GitLab is also published within the application at /help on the domain of the GitLab instance.
At /help, only help for your current edition and version is included. Help for other versions is available at https://docs.gitlab.com/archives/.
The source of the documentation exists within the codebase of each GitLab application in the following repository locations:



Project
Path




GitLab
/doc


GitLab Runner
/docs


Omnibus GitLab
/doc


Charts
/doc


GitLab Operator
/doc



Documentation issues and merge requests are part of their respective repositories and all have the label Documentation.

Branch naming
The CI pipeline for the main GitLab project is configured to automatically
run only the jobs that match the type of contribution. If your contribution contains
only documentation changes, then only documentation-related jobs run, and
the pipeline completes much faster than a code contribution.
If you are submitting documentation-only changes to Omnibus or Charts,
the fast pipeline is not determined automatically. Instead, create branches for
docs-only merge requests using the following guide:



Branch name
Valid example




Starting with docs/

docs/update-api-issues


Starting with docs-

docs-update-api-issues


Ending in -docs

123-update-api-issues-docs




Contributing to docs
Contributions to GitLab docs are welcome from the entire GitLab community.
To ensure that the GitLab docs are current, there are special processes and responsibilities for all feature changes, that is development work that impacts the appearance, usage, or administration of a feature.
However, anyone can contribute documentation improvements that are not associated with a feature change. For example, adding a new document on how to accomplish a use case that's already possible with GitLab or with third-party tools and GitLab.

Markdown and styles
GitLab docs uses GitLab Kramdown
as its Markdown rendering engine. See the GitLab Markdown Guide for a complete Kramdown reference.
Adhere to the Documentation Style Guide. If a style standard is missing, you are welcome to suggest one via a merge request.

Folder structure and files
See the Folder structure page.

Metadata
To provide additional directives or useful information, we add metadata in YAML
format to the beginning of each product documentation page (YAML front matter).
All values are treated as strings and are only used for the
docs website.

Stage and group metadata
Each page should ideally have metadata related to the stage and group it
belongs to, as well as an information block as described below:


stage: The Stage
to which the majority of the page's content belongs.


group: The Group
to which the majority of the page's content belongs.


info: The following line, which provides direction to contributors regarding
how to contact the Technical Writer associated with the page's stage and
group:

To determine the technical writer assigned to the Stage/Group
associated with this page, see
https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments




For example:

---
stage: Example Stage
group: Example Group
info: To determine the technical writer assigned to the Stage/Group associated with this page, see https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments
---


If you need help determining the correct stage, read Ask for help.

Redirection metadata
The following metadata should be added when a page is moved to another location:


redirect_to: The relative path and filename (with an .md extension) of the
location to which visitors should be redirected for a moved page.
Learn more.

disqus_identifier: Identifier for Disqus commenting system. Used to keep
comments with a page that has been moved to a new URL.
Learn more.


Comments metadata
The docs website has comments (provided by Disqus)
enabled by default. In case you want to disable them (for example in index pages),
set it to false:

---
comments: false
---



Additional page metadata
Each page can have additional, optional metadata (set in the
default.html
Nanoc layout), which is displayed at the top of the page if defined.

Deprecated metadata
The type metadata parameter is deprecated but still exists in documentation
pages. You can safely remove the type metadata parameter and its values.

Batch updates for TW metadata
NOTE:
This task is an MVC, and requires significant manual preparation of the output.
While the task can be time consuming, it is still faster than doing the work
entirely manually.
It's important to keep the CODEOWNERS
file in the gitlab project up to date with the current Technical Writing team assignments.
This information is used in merge requests that contain documentation:

To populate the eligible approvers section.
By GitLab Bot to ping reviewers for community contributions.

GitLab cannot automatically associate the stage and group metadata in our documentation
pages with the technical writer assigned to that group, so we use a Rake task to
generate entries for the CODEOWNERS file. Declaring code owners for pages reduces
the number of times GitLab Bot pings the entire Technical Writing team.
The tw:codeowners Rake task, located in lib/tasks/gitlab/tw/codeowners.rake,
contains an array of groups and their assigned technical writer. This task:

Outputs a line for each doc with metadata that matches a group in lib/tasks/gitlab/tw/codeowners.rake.
Files not matching a group are skipped.
Adds the full path to the page, and the assigned technical writer.

To prepare an update to the CODEOWNERS file:


Update lib/tasks/gitlab/tw/codeowners.rake with the latest TW team assignments.
Make this update in a standalone merge request, as it runs a long pipeline and
requires backend maintainer review. Make sure this is merged before you update
CODEOWNERS in another merge request.


Run the task from the root directory of the gitlab repository, and save the output in a file:

bundle exec rake tw:codeowners > ~/Desktop/updates.md




Open the file you just created (~/Desktop/updates.md in this example), and prepare the output:

Find and replace ./ with /.
Sort the lines in alphabetical (ascending) order. If you use VS Code, you can
select everything, press F1, type sort, and select Sort lines (ascending, case insensitive.



Create a new branch for your CODEOWNERS updates.


Replace the documentation-related lines in the ^[Documentation Pages] section
with the output you prepared.
WARNING:
The documentation section is not the last section of the CODEOWNERS file. Don't
delete data that isn't ours!


Create a commit with the raw changes.


From the command line, run git diff master.


In the diff, look for directory-level assignments to manually restore to the
CODEOWNERS file. If all files in a single directory are assigned to the same
technical writer, we simplify these entries. Remove all the lines for the individual
files, and leave a single entry for the directory, for example: /doc/directory/ @tech.writer.


In the diff, look for changes that don't match your expectations:

New pages, or newly moved pages, show up as added lines.
Deleted pages, and pages that are now redirects, show up as deleted lines.
If you see an unusual number of changes to pages that all seem related,
check the metadata for the pages. A group might have been renamed and the Rake task
must be updated to match.



Create another commit with your manual changes, and create a second merge request
with your changes to the CODEOWNERS file. Assign it to a technical writing manager for review.



Move, rename, or delete a page
See redirects.

Merge requests for GitLab documentation
Before getting started, make sure you read the introductory section
"contributing to docs" above and the
documentation workflow.

Use the current merge request description template

Label the MR Documentation (can only be done by people with developer access, for example, GitLab team members)
Assign the correct milestone per note below (can only be done by people with developer access, for example, GitLab team members)

Documentation is merged if it is an improvement on existing content,
represents a good-faith effort to follow the template and style standards,
and is believed to be accurate.
Further needs for what would make the doc even better should be immediately addressed
in a follow-up merge request or issue.
If the release version you want to add the documentation to has already been
frozen or released, use the label ~"Pick into X.Y" to get it merged into
the correct release. Avoid picking into a past release as much as you can, as
it increases the work of the release managers.

GitLab /help

Every GitLab instance includes documentation at /help (https://gitlab.example.com/help)
that matches the version of the instance. For example, https://gitlab.com/help.
The documentation available online at https://docs.gitlab.com is deployed every
four hours from the default branch of GitLab, Omnibus, Runner, and Charts.
After a merge request that updates documentation is merged, it is available online
in 4 hours or less.
However, it's only available at /help on self-managed instances in the next released
version. The date an update is merged can impact which self-managed release the update
is present in.
For example:

A merge request in gitlab updates documentation. It has a milestone of 14.4,
with an expected release date of 2021-10-22.
It is merged on 2021-10-19 and available online the same day at https://docs.gitlab.com.
GitLab 14.4 is released on 2021-10-22, based on the gitlab codebase from 2021-10-18
(one day before the update was merged).
The change shows up in the 14.5 self-managed release, due to missing the release cutoff
for 14.4.

The exact cutoff date for each release is flexible, and can be sooner or later
than expected due to holidays, weekends or other events. In general, MRs merged
by the 17th should be present in the release on the 22nd, though it is not guaranteed.
If it is important that a documentation update is present in that month's release,
merge it as early as possible.

Linking to /help

When you're building a new feature, you may need to link to the documentation
from the GitLab application. This is normally done in files inside the
app/views/ directory, with the help of the help_page_path helper method.
The help_page_path contains the path to the document you want to link to,
with the following conventions:

It's relative to the doc/ directory in the GitLab repository.
It omits the .md extension.
It doesn't end with a forward slash (/).

The help text follows the Pajamas guidelines.

Linking to /help in HAML
Use the following special cases depending on the context, ensuring all link text
is inside _() so it can be translated:


Linking to a doc page. In its most basic form, the HAML code to generate a
link to the /help page is:

= link_to _('Learn more.'), help_page_path('user/permissions'), target: '_blank', rel: 'noopener noreferrer'




Linking to an anchor link. Use anchor as part of the help_page_path
method:

= link_to _('Learn more.'), help_page_path('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'




Using links inline of some text. First, define the link, and then use it. In
this example, link_start is the name of the variable that contains the
link:

- link_start = '<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe % { url: help_page_path('user/permissions') }
%p= _("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe % { link_start: link_start, link_end: '</a>'.html_safe }




Using a button link. Useful in places where text would be out of context with
the rest of the page layout:

= link_to _('Learn more.'), help_page_path('user/permissions'),  class: 'btn btn-info', target: '_blank', rel: 'noopener noreferrer'





Linking to /help in JavaScript
To link to the documentation from a JavaScript or a Vue component, use the helpPagePath function from help_page_helper.js:

import { helpPagePath } from '~/helpers/help_page_helper';

helpPagePath('user/permissions', { anchor: 'anchor-link' })
// evaluates to '/help/user/permissions#anchor-link' for GitLab.com


This is preferred over static paths, as the helper also works on instances installed under a relative URL.

Linking to /help in Ruby
To link to the documentation from within Ruby code, use the following code block as a guide, ensuring all link text is inside _() so it can
be translated:

docs_link = link_to _('Learn more.'), help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


In cases where you need to generate a link from outside of views/helpers, where the link_to and help_page_url methods are not available, use the following code block
as a guide where the methods are fully qualified:

docs_link = ActionController::Base.helpers.link_to _('Learn more.'), Rails.application.routes.url_helpers.help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


Do not use include ActionView::Helpers::UrlHelper just to make the link_to method available as you might see in some existing code. Read more in
issue 340567.

GitLab /help tests
Several RSpec tests
are run to ensure GitLab documentation renders and works correctly. In particular, that main docs landing page works correctly from /help.
For example, GitLab.com's /help.

Docs site architecture
For information on how we build and deploy https://docs.gitlab.com, see Docs site architecture.

Global navigation
See the Global navigation doc for information
on how the left-side navigation menu is built and updated.

Previewing the changes live
See how you can use review apps to preview your changes live.

Testing
For more information about documentation testing, see the Documentation testing
guide.

Danger Bot
GitLab uses Danger for some elements in
code review. For docs changes in merge requests, whenever a change to files under /doc
is made, Danger Bot leaves a comment with further instructions about the documentation
process. This is configured in the Dangerfile in the GitLab repository under
/danger/documentation/.

Automatic screenshot generator
You can now set up an automatic screenshot generator to take and compress screenshots with the
help of a configuration file known as screenshot generator.

Use the tool
To run the tool on an existing screenshot generator, take the following steps:

Set up the GitLab Development Kit (GDK).
Navigate to the subdirectory with your cloned GitLab repository, typically gdk/gitlab.
Make sure that your GDK database is fully migrated: bin/rake db:migrate RAILS_ENV=development.
Install pngquant, see the tool website for more information: pngquant

Run scripts/docs_screenshots.rb spec/docs_screenshots/<name_of_screenshot_generator>.rb <milestone-version>.
Identify the location of the screenshots, based on the gitlab/doc location defined by the it parameter in your script.
Commit the newly created screenshots.


Extending the tool
To add an additional screenshot generator, take the following steps:

Locate the spec/docs_screenshots directory.
Add a new file with a _docs.rb extension.
Be sure to include the following bits in the file:


require 'spec_helper'

RSpec.describe '<What I am taking screenshots of>', :js do
  include DocsScreenshotHelpers # Helper that enables the screenshots taking mechanism

  before do
    page.driver.browser.manage.window.resize_to(1366, 1024) # length and width of the page
  end



In addition, every it block must include the path where the screenshot is saved


 it 'user/packages/container_registry/img/project_image_repositories_list'



Full page screenshots
To take a full page screenshot simply visit the page and perform any expectation on real content (to have capybara wait till the page is ready and not take a white screenshot).

Element screenshot
To have the screenshot focuses few more steps are needed:


find the area: screenshot_area = find('#js-registry-policies')


scroll the area in focus: scroll_to screenshot_area


wait for the content: expect(screenshot_area).to have_content 'Expiration interval'


set the crop area: set_crop_data(screenshot_area, 20)


In particular, set_crop_data accepts as arguments: a DOM element and a
padding. The padding is added around the element, enlarging the screenshot area.

Live example
Please use spec/docs_screenshots/container_registry_docs.rb as a guide and as an example to create your own scripts.








Help

Help









Help

Help






Help

Help


Help
Help






GitLab Documentation guidelines
The GitLab documentation is intended as the single source of truth (SSOT) for information about how to configure, use, and troubleshoot GitLab. The documentation contains use cases and usage instructions for every GitLab feature, organized by product area and subject. This includes topics and workflows that span multiple GitLab features and the use of GitLab with other applications.
In addition to this page, the following resources can help you craft and contribute to documentation:


Style Guide - What belongs in the docs, language guidelines, Markdown standards to follow, links, and more.

Topic type template - Learn about the different types of topics.

Documentation process.

Markdown Guide - A reference for all Markdown syntax supported by GitLab.

Site architecture - How https://docs.gitlab.com is built.

Documentation for feature flags - How to write and update documentation for GitLab features deployed behind feature flags.


Source files and rendered web locations
Documentation for GitLab, GitLab Runner, GitLab Operator, Omnibus GitLab, and Charts is published to https://docs.gitlab.com. Documentation for GitLab is also published within the application at /help on the domain of the GitLab instance.
At /help, only help for your current edition and version is included. Help for other versions is available at https://docs.gitlab.com/archives/.
The source of the documentation exists within the codebase of each GitLab application in the following repository locations:



Project
Path




GitLab
/doc


GitLab Runner
/docs


Omnibus GitLab
/doc


Charts
/doc


GitLab Operator
/doc



Documentation issues and merge requests are part of their respective repositories and all have the label Documentation.

Branch naming
The CI pipeline for the main GitLab project is configured to automatically
run only the jobs that match the type of contribution. If your contribution contains
only documentation changes, then only documentation-related jobs run, and
the pipeline completes much faster than a code contribution.
If you are submitting documentation-only changes to Omnibus or Charts,
the fast pipeline is not determined automatically. Instead, create branches for
docs-only merge requests using the following guide:



Branch name
Valid example




Starting with docs/

docs/update-api-issues


Starting with docs-

docs-update-api-issues


Ending in -docs

123-update-api-issues-docs




Contributing to docs
Contributions to GitLab docs are welcome from the entire GitLab community.
To ensure that the GitLab docs are current, there are special processes and responsibilities for all feature changes, that is development work that impacts the appearance, usage, or administration of a feature.
However, anyone can contribute documentation improvements that are not associated with a feature change. For example, adding a new document on how to accomplish a use case that's already possible with GitLab or with third-party tools and GitLab.

Markdown and styles
GitLab docs uses GitLab Kramdown
as its Markdown rendering engine. See the GitLab Markdown Guide for a complete Kramdown reference.
Adhere to the Documentation Style Guide. If a style standard is missing, you are welcome to suggest one via a merge request.

Folder structure and files
See the Folder structure page.

Metadata
To provide additional directives or useful information, we add metadata in YAML
format to the beginning of each product documentation page (YAML front matter).
All values are treated as strings and are only used for the
docs website.

Stage and group metadata
Each page should ideally have metadata related to the stage and group it
belongs to, as well as an information block as described below:


stage: The Stage
to which the majority of the page's content belongs.


group: The Group
to which the majority of the page's content belongs.


info: The following line, which provides direction to contributors regarding
how to contact the Technical Writer associated with the page's stage and
group:

To determine the technical writer assigned to the Stage/Group
associated with this page, see
https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments




For example:

---
stage: Example Stage
group: Example Group
info: To determine the technical writer assigned to the Stage/Group associated with this page, see https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments
---


If you need help determining the correct stage, read Ask for help.

Redirection metadata
The following metadata should be added when a page is moved to another location:


redirect_to: The relative path and filename (with an .md extension) of the
location to which visitors should be redirected for a moved page.
Learn more.

disqus_identifier: Identifier for Disqus commenting system. Used to keep
comments with a page that has been moved to a new URL.
Learn more.


Comments metadata
The docs website has comments (provided by Disqus)
enabled by default. In case you want to disable them (for example in index pages),
set it to false:

---
comments: false
---



Additional page metadata
Each page can have additional, optional metadata (set in the
default.html
Nanoc layout), which is displayed at the top of the page if defined.

Deprecated metadata
The type metadata parameter is deprecated but still exists in documentation
pages. You can safely remove the type metadata parameter and its values.

Batch updates for TW metadata
NOTE:
This task is an MVC, and requires significant manual preparation of the output.
While the task can be time consuming, it is still faster than doing the work
entirely manually.
It's important to keep the CODEOWNERS
file in the gitlab project up to date with the current Technical Writing team assignments.
This information is used in merge requests that contain documentation:

To populate the eligible approvers section.
By GitLab Bot to ping reviewers for community contributions.

GitLab cannot automatically associate the stage and group metadata in our documentation
pages with the technical writer assigned to that group, so we use a Rake task to
generate entries for the CODEOWNERS file. Declaring code owners for pages reduces
the number of times GitLab Bot pings the entire Technical Writing team.
The tw:codeowners Rake task, located in lib/tasks/gitlab/tw/codeowners.rake,
contains an array of groups and their assigned technical writer. This task:

Outputs a line for each doc with metadata that matches a group in lib/tasks/gitlab/tw/codeowners.rake.
Files not matching a group are skipped.
Adds the full path to the page, and the assigned technical writer.

To prepare an update to the CODEOWNERS file:


Update lib/tasks/gitlab/tw/codeowners.rake with the latest TW team assignments.
Make this update in a standalone merge request, as it runs a long pipeline and
requires backend maintainer review. Make sure this is merged before you update
CODEOWNERS in another merge request.


Run the task from the root directory of the gitlab repository, and save the output in a file:

bundle exec rake tw:codeowners > ~/Desktop/updates.md




Open the file you just created (~/Desktop/updates.md in this example), and prepare the output:

Find and replace ./ with /.
Sort the lines in alphabetical (ascending) order. If you use VS Code, you can
select everything, press F1, type sort, and select Sort lines (ascending, case insensitive.



Create a new branch for your CODEOWNERS updates.


Replace the documentation-related lines in the ^[Documentation Pages] section
with the output you prepared.
WARNING:
The documentation section is not the last section of the CODEOWNERS file. Don't
delete data that isn't ours!


Create a commit with the raw changes.


From the command line, run git diff master.


In the diff, look for directory-level assignments to manually restore to the
CODEOWNERS file. If all files in a single directory are assigned to the same
technical writer, we simplify these entries. Remove all the lines for the individual
files, and leave a single entry for the directory, for example: /doc/directory/ @tech.writer.


In the diff, look for changes that don't match your expectations:

New pages, or newly moved pages, show up as added lines.
Deleted pages, and pages that are now redirects, show up as deleted lines.
If you see an unusual number of changes to pages that all seem related,
check the metadata for the pages. A group might have been renamed and the Rake task
must be updated to match.



Create another commit with your manual changes, and create a second merge request
with your changes to the CODEOWNERS file. Assign it to a technical writing manager for review.



Move, rename, or delete a page
See redirects.

Merge requests for GitLab documentation
Before getting started, make sure you read the introductory section
"contributing to docs" above and the
documentation workflow.

Use the current merge request description template

Label the MR Documentation (can only be done by people with developer access, for example, GitLab team members)
Assign the correct milestone per note below (can only be done by people with developer access, for example, GitLab team members)

Documentation is merged if it is an improvement on existing content,
represents a good-faith effort to follow the template and style standards,
and is believed to be accurate.
Further needs for what would make the doc even better should be immediately addressed
in a follow-up merge request or issue.
If the release version you want to add the documentation to has already been
frozen or released, use the label ~"Pick into X.Y" to get it merged into
the correct release. Avoid picking into a past release as much as you can, as
it increases the work of the release managers.

GitLab /help

Every GitLab instance includes documentation at /help (https://gitlab.example.com/help)
that matches the version of the instance. For example, https://gitlab.com/help.
The documentation available online at https://docs.gitlab.com is deployed every
four hours from the default branch of GitLab, Omnibus, Runner, and Charts.
After a merge request that updates documentation is merged, it is available online
in 4 hours or less.
However, it's only available at /help on self-managed instances in the next released
version. The date an update is merged can impact which self-managed release the update
is present in.
For example:

A merge request in gitlab updates documentation. It has a milestone of 14.4,
with an expected release date of 2021-10-22.
It is merged on 2021-10-19 and available online the same day at https://docs.gitlab.com.
GitLab 14.4 is released on 2021-10-22, based on the gitlab codebase from 2021-10-18
(one day before the update was merged).
The change shows up in the 14.5 self-managed release, due to missing the release cutoff
for 14.4.

The exact cutoff date for each release is flexible, and can be sooner or later
than expected due to holidays, weekends or other events. In general, MRs merged
by the 17th should be present in the release on the 22nd, though it is not guaranteed.
If it is important that a documentation update is present in that month's release,
merge it as early as possible.

Linking to /help

When you're building a new feature, you may need to link to the documentation
from the GitLab application. This is normally done in files inside the
app/views/ directory, with the help of the help_page_path helper method.
The help_page_path contains the path to the document you want to link to,
with the following conventions:

It's relative to the doc/ directory in the GitLab repository.
It omits the .md extension.
It doesn't end with a forward slash (/).

The help text follows the Pajamas guidelines.

Linking to /help in HAML
Use the following special cases depending on the context, ensuring all link text
is inside _() so it can be translated:


Linking to a doc page. In its most basic form, the HAML code to generate a
link to the /help page is:

= link_to _('Learn more.'), help_page_path('user/permissions'), target: '_blank', rel: 'noopener noreferrer'




Linking to an anchor link. Use anchor as part of the help_page_path
method:

= link_to _('Learn more.'), help_page_path('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'




Using links inline of some text. First, define the link, and then use it. In
this example, link_start is the name of the variable that contains the
link:

- link_start = '<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe % { url: help_page_path('user/permissions') }
%p= _("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe % { link_start: link_start, link_end: '</a>'.html_safe }




Using a button link. Useful in places where text would be out of context with
the rest of the page layout:

= link_to _('Learn more.'), help_page_path('user/permissions'),  class: 'btn btn-info', target: '_blank', rel: 'noopener noreferrer'





Linking to /help in JavaScript
To link to the documentation from a JavaScript or a Vue component, use the helpPagePath function from help_page_helper.js:

import { helpPagePath } from '~/helpers/help_page_helper';

helpPagePath('user/permissions', { anchor: 'anchor-link' })
// evaluates to '/help/user/permissions#anchor-link' for GitLab.com


This is preferred over static paths, as the helper also works on instances installed under a relative URL.

Linking to /help in Ruby
To link to the documentation from within Ruby code, use the following code block as a guide, ensuring all link text is inside _() so it can
be translated:

docs_link = link_to _('Learn more.'), help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


In cases where you need to generate a link from outside of views/helpers, where the link_to and help_page_url methods are not available, use the following code block
as a guide where the methods are fully qualified:

docs_link = ActionController::Base.helpers.link_to _('Learn more.'), Rails.application.routes.url_helpers.help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


Do not use include ActionView::Helpers::UrlHelper just to make the link_to method available as you might see in some existing code. Read more in
issue 340567.

GitLab /help tests
Several RSpec tests
are run to ensure GitLab documentation renders and works correctly. In particular, that main docs landing page works correctly from /help.
For example, GitLab.com's /help.

Docs site architecture
For information on how we build and deploy https://docs.gitlab.com, see Docs site architecture.

Global navigation
See the Global navigation doc for information
on how the left-side navigation menu is built and updated.

Previewing the changes live
See how you can use review apps to preview your changes live.

Testing
For more information about documentation testing, see the Documentation testing
guide.

Danger Bot
GitLab uses Danger for some elements in
code review. For docs changes in merge requests, whenever a change to files under /doc
is made, Danger Bot leaves a comment with further instructions about the documentation
process. This is configured in the Dangerfile in the GitLab repository under
/danger/documentation/.

Automatic screenshot generator
You can now set up an automatic screenshot generator to take and compress screenshots with the
help of a configuration file known as screenshot generator.

Use the tool
To run the tool on an existing screenshot generator, take the following steps:

Set up the GitLab Development Kit (GDK).
Navigate to the subdirectory with your cloned GitLab repository, typically gdk/gitlab.
Make sure that your GDK database is fully migrated: bin/rake db:migrate RAILS_ENV=development.
Install pngquant, see the tool website for more information: pngquant

Run scripts/docs_screenshots.rb spec/docs_screenshots/<name_of_screenshot_generator>.rb <milestone-version>.
Identify the location of the screenshots, based on the gitlab/doc location defined by the it parameter in your script.
Commit the newly created screenshots.


Extending the tool
To add an additional screenshot generator, take the following steps:

Locate the spec/docs_screenshots directory.
Add a new file with a _docs.rb extension.
Be sure to include the following bits in the file:


require 'spec_helper'

RSpec.describe '<What I am taking screenshots of>', :js do
  include DocsScreenshotHelpers # Helper that enables the screenshots taking mechanism

  before do
    page.driver.browser.manage.window.resize_to(1366, 1024) # length and width of the page
  end



In addition, every it block must include the path where the screenshot is saved


 it 'user/packages/container_registry/img/project_image_repositories_list'



Full page screenshots
To take a full page screenshot simply visit the page and perform any expectation on real content (to have capybara wait till the page is ready and not take a white screenshot).

Element screenshot
To have the screenshot focuses few more steps are needed:


find the area: screenshot_area = find('#js-registry-policies')


scroll the area in focus: scroll_to screenshot_area


wait for the content: expect(screenshot_area).to have_content 'Expiration interval'


set the crop area: set_crop_data(screenshot_area, 20)


In particular, set_crop_data accepts as arguments: a DOM element and a
padding. The padding is added around the element, enlarging the screenshot area.

Live example
Please use spec/docs_screenshots/container_registry_docs.rb as a guide and as an example to create your own scripts.





GitLab Documentation guidelines
The GitLab documentation is intended as the single source of truth (SSOT) for information about how to configure, use, and troubleshoot GitLab. The documentation contains use cases and usage instructions for every GitLab feature, organized by product area and subject. This includes topics and workflows that span multiple GitLab features and the use of GitLab with other applications.
In addition to this page, the following resources can help you craft and contribute to documentation:


Style Guide - What belongs in the docs, language guidelines, Markdown standards to follow, links, and more.

Topic type template - Learn about the different types of topics.

Documentation process.

Markdown Guide - A reference for all Markdown syntax supported by GitLab.

Site architecture - How https://docs.gitlab.com is built.

Documentation for feature flags - How to write and update documentation for GitLab features deployed behind feature flags.


Source files and rendered web locations
Documentation for GitLab, GitLab Runner, GitLab Operator, Omnibus GitLab, and Charts is published to https://docs.gitlab.com. Documentation for GitLab is also published within the application at /help on the domain of the GitLab instance.
At /help, only help for your current edition and version is included. Help for other versions is available at https://docs.gitlab.com/archives/.
The source of the documentation exists within the codebase of each GitLab application in the following repository locations:



Project
Path




GitLab
/doc


GitLab Runner
/docs


Omnibus GitLab
/doc


Charts
/doc


GitLab Operator
/doc



Documentation issues and merge requests are part of their respective repositories and all have the label Documentation.

Branch naming
The CI pipeline for the main GitLab project is configured to automatically
run only the jobs that match the type of contribution. If your contribution contains
only documentation changes, then only documentation-related jobs run, and
the pipeline completes much faster than a code contribution.
If you are submitting documentation-only changes to Omnibus or Charts,
the fast pipeline is not determined automatically. Instead, create branches for
docs-only merge requests using the following guide:



Branch name
Valid example




Starting with docs/

docs/update-api-issues


Starting with docs-

docs-update-api-issues


Ending in -docs

123-update-api-issues-docs




Contributing to docs
Contributions to GitLab docs are welcome from the entire GitLab community.
To ensure that the GitLab docs are current, there are special processes and responsibilities for all feature changes, that is development work that impacts the appearance, usage, or administration of a feature.
However, anyone can contribute documentation improvements that are not associated with a feature change. For example, adding a new document on how to accomplish a use case that's already possible with GitLab or with third-party tools and GitLab.

Markdown and styles
GitLab docs uses GitLab Kramdown
as its Markdown rendering engine. See the GitLab Markdown Guide for a complete Kramdown reference.
Adhere to the Documentation Style Guide. If a style standard is missing, you are welcome to suggest one via a merge request.

Folder structure and files
See the Folder structure page.

Metadata
To provide additional directives or useful information, we add metadata in YAML
format to the beginning of each product documentation page (YAML front matter).
All values are treated as strings and are only used for the
docs website.

Stage and group metadata
Each page should ideally have metadata related to the stage and group it
belongs to, as well as an information block as described below:


stage: The Stage
to which the majority of the page's content belongs.


group: The Group
to which the majority of the page's content belongs.


info: The following line, which provides direction to contributors regarding
how to contact the Technical Writer associated with the page's stage and
group:

To determine the technical writer assigned to the Stage/Group
associated with this page, see
https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments




For example:

---
stage: Example Stage
group: Example Group
info: To determine the technical writer assigned to the Stage/Group associated with this page, see https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments
---


If you need help determining the correct stage, read Ask for help.

Redirection metadata
The following metadata should be added when a page is moved to another location:


redirect_to: The relative path and filename (with an .md extension) of the
location to which visitors should be redirected for a moved page.
Learn more.

disqus_identifier: Identifier for Disqus commenting system. Used to keep
comments with a page that has been moved to a new URL.
Learn more.


Comments metadata
The docs website has comments (provided by Disqus)
enabled by default. In case you want to disable them (for example in index pages),
set it to false:

---
comments: false
---



Additional page metadata
Each page can have additional, optional metadata (set in the
default.html
Nanoc layout), which is displayed at the top of the page if defined.

Deprecated metadata
The type metadata parameter is deprecated but still exists in documentation
pages. You can safely remove the type metadata parameter and its values.

Batch updates for TW metadata
NOTE:
This task is an MVC, and requires significant manual preparation of the output.
While the task can be time consuming, it is still faster than doing the work
entirely manually.
It's important to keep the CODEOWNERS
file in the gitlab project up to date with the current Technical Writing team assignments.
This information is used in merge requests that contain documentation:

To populate the eligible approvers section.
By GitLab Bot to ping reviewers for community contributions.

GitLab cannot automatically associate the stage and group metadata in our documentation
pages with the technical writer assigned to that group, so we use a Rake task to
generate entries for the CODEOWNERS file. Declaring code owners for pages reduces
the number of times GitLab Bot pings the entire Technical Writing team.
The tw:codeowners Rake task, located in lib/tasks/gitlab/tw/codeowners.rake,
contains an array of groups and their assigned technical writer. This task:

Outputs a line for each doc with metadata that matches a group in lib/tasks/gitlab/tw/codeowners.rake.
Files not matching a group are skipped.
Adds the full path to the page, and the assigned technical writer.

To prepare an update to the CODEOWNERS file:


Update lib/tasks/gitlab/tw/codeowners.rake with the latest TW team assignments.
Make this update in a standalone merge request, as it runs a long pipeline and
requires backend maintainer review. Make sure this is merged before you update
CODEOWNERS in another merge request.


Run the task from the root directory of the gitlab repository, and save the output in a file:

bundle exec rake tw:codeowners > ~/Desktop/updates.md




Open the file you just created (~/Desktop/updates.md in this example), and prepare the output:

Find and replace ./ with /.
Sort the lines in alphabetical (ascending) order. If you use VS Code, you can
select everything, press F1, type sort, and select Sort lines (ascending, case insensitive.



Create a new branch for your CODEOWNERS updates.


Replace the documentation-related lines in the ^[Documentation Pages] section
with the output you prepared.
WARNING:
The documentation section is not the last section of the CODEOWNERS file. Don't
delete data that isn't ours!


Create a commit with the raw changes.


From the command line, run git diff master.


In the diff, look for directory-level assignments to manually restore to the
CODEOWNERS file. If all files in a single directory are assigned to the same
technical writer, we simplify these entries. Remove all the lines for the individual
files, and leave a single entry for the directory, for example: /doc/directory/ @tech.writer.


In the diff, look for changes that don't match your expectations:

New pages, or newly moved pages, show up as added lines.
Deleted pages, and pages that are now redirects, show up as deleted lines.
If you see an unusual number of changes to pages that all seem related,
check the metadata for the pages. A group might have been renamed and the Rake task
must be updated to match.



Create another commit with your manual changes, and create a second merge request
with your changes to the CODEOWNERS file. Assign it to a technical writing manager for review.



Move, rename, or delete a page
See redirects.

Merge requests for GitLab documentation
Before getting started, make sure you read the introductory section
"contributing to docs" above and the
documentation workflow.

Use the current merge request description template

Label the MR Documentation (can only be done by people with developer access, for example, GitLab team members)
Assign the correct milestone per note below (can only be done by people with developer access, for example, GitLab team members)

Documentation is merged if it is an improvement on existing content,
represents a good-faith effort to follow the template and style standards,
and is believed to be accurate.
Further needs for what would make the doc even better should be immediately addressed
in a follow-up merge request or issue.
If the release version you want to add the documentation to has already been
frozen or released, use the label ~"Pick into X.Y" to get it merged into
the correct release. Avoid picking into a past release as much as you can, as
it increases the work of the release managers.

GitLab /help

Every GitLab instance includes documentation at /help (https://gitlab.example.com/help)
that matches the version of the instance. For example, https://gitlab.com/help.
The documentation available online at https://docs.gitlab.com is deployed every
four hours from the default branch of GitLab, Omnibus, Runner, and Charts.
After a merge request that updates documentation is merged, it is available online
in 4 hours or less.
However, it's only available at /help on self-managed instances in the next released
version. The date an update is merged can impact which self-managed release the update
is present in.
For example:

A merge request in gitlab updates documentation. It has a milestone of 14.4,
with an expected release date of 2021-10-22.
It is merged on 2021-10-19 and available online the same day at https://docs.gitlab.com.
GitLab 14.4 is released on 2021-10-22, based on the gitlab codebase from 2021-10-18
(one day before the update was merged).
The change shows up in the 14.5 self-managed release, due to missing the release cutoff
for 14.4.

The exact cutoff date for each release is flexible, and can be sooner or later
than expected due to holidays, weekends or other events. In general, MRs merged
by the 17th should be present in the release on the 22nd, though it is not guaranteed.
If it is important that a documentation update is present in that month's release,
merge it as early as possible.

Linking to /help

When you're building a new feature, you may need to link to the documentation
from the GitLab application. This is normally done in files inside the
app/views/ directory, with the help of the help_page_path helper method.
The help_page_path contains the path to the document you want to link to,
with the following conventions:

It's relative to the doc/ directory in the GitLab repository.
It omits the .md extension.
It doesn't end with a forward slash (/).

The help text follows the Pajamas guidelines.

Linking to /help in HAML
Use the following special cases depending on the context, ensuring all link text
is inside _() so it can be translated:


Linking to a doc page. In its most basic form, the HAML code to generate a
link to the /help page is:

= link_to _('Learn more.'), help_page_path('user/permissions'), target: '_blank', rel: 'noopener noreferrer'




Linking to an anchor link. Use anchor as part of the help_page_path
method:

= link_to _('Learn more.'), help_page_path('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'




Using links inline of some text. First, define the link, and then use it. In
this example, link_start is the name of the variable that contains the
link:

- link_start = '<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe % { url: help_page_path('user/permissions') }
%p= _("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe % { link_start: link_start, link_end: '</a>'.html_safe }




Using a button link. Useful in places where text would be out of context with
the rest of the page layout:

= link_to _('Learn more.'), help_page_path('user/permissions'),  class: 'btn btn-info', target: '_blank', rel: 'noopener noreferrer'





Linking to /help in JavaScript
To link to the documentation from a JavaScript or a Vue component, use the helpPagePath function from help_page_helper.js:

import { helpPagePath } from '~/helpers/help_page_helper';

helpPagePath('user/permissions', { anchor: 'anchor-link' })
// evaluates to '/help/user/permissions#anchor-link' for GitLab.com


This is preferred over static paths, as the helper also works on instances installed under a relative URL.

Linking to /help in Ruby
To link to the documentation from within Ruby code, use the following code block as a guide, ensuring all link text is inside _() so it can
be translated:

docs_link = link_to _('Learn more.'), help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


In cases where you need to generate a link from outside of views/helpers, where the link_to and help_page_url methods are not available, use the following code block
as a guide where the methods are fully qualified:

docs_link = ActionController::Base.helpers.link_to _('Learn more.'), Rails.application.routes.url_helpers.help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }


Do not use include ActionView::Helpers::UrlHelper just to make the link_to method available as you might see in some existing code. Read more in
issue 340567.

GitLab /help tests
Several RSpec tests
are run to ensure GitLab documentation renders and works correctly. In particular, that main docs landing page works correctly from /help.
For example, GitLab.com's /help.

Docs site architecture
For information on how we build and deploy https://docs.gitlab.com, see Docs site architecture.

Global navigation
See the Global navigation doc for information
on how the left-side navigation menu is built and updated.

Previewing the changes live
See how you can use review apps to preview your changes live.

Testing
For more information about documentation testing, see the Documentation testing
guide.

Danger Bot
GitLab uses Danger for some elements in
code review. For docs changes in merge requests, whenever a change to files under /doc
is made, Danger Bot leaves a comment with further instructions about the documentation
process. This is configured in the Dangerfile in the GitLab repository under
/danger/documentation/.

Automatic screenshot generator
You can now set up an automatic screenshot generator to take and compress screenshots with the
help of a configuration file known as screenshot generator.

Use the tool
To run the tool on an existing screenshot generator, take the following steps:

Set up the GitLab Development Kit (GDK).
Navigate to the subdirectory with your cloned GitLab repository, typically gdk/gitlab.
Make sure that your GDK database is fully migrated: bin/rake db:migrate RAILS_ENV=development.
Install pngquant, see the tool website for more information: pngquant

Run scripts/docs_screenshots.rb spec/docs_screenshots/<name_of_screenshot_generator>.rb <milestone-version>.
Identify the location of the screenshots, based on the gitlab/doc location defined by the it parameter in your script.
Commit the newly created screenshots.


Extending the tool
To add an additional screenshot generator, take the following steps:

Locate the spec/docs_screenshots directory.
Add a new file with a _docs.rb extension.
Be sure to include the following bits in the file:


require 'spec_helper'

RSpec.describe '<What I am taking screenshots of>', :js do
  include DocsScreenshotHelpers # Helper that enables the screenshots taking mechanism

  before do
    page.driver.browser.manage.window.resize_to(1366, 1024) # length and width of the page
  end



In addition, every it block must include the path where the screenshot is saved


 it 'user/packages/container_registry/img/project_image_repositories_list'



Full page screenshots
To take a full page screenshot simply visit the page and perform any expectation on real content (to have capybara wait till the page is ready and not take a white screenshot).

Element screenshot
To have the screenshot focuses few more steps are needed:


find the area: screenshot_area = find('#js-registry-policies')


scroll the area in focus: scroll_to screenshot_area


wait for the content: expect(screenshot_area).to have_content 'Expiration interval'


set the crop area: set_crop_data(screenshot_area, 20)


In particular, set_crop_data accepts as arguments: a DOM element and a
padding. The padding is added around the element, enlarging the screenshot area.

Live example
Please use spec/docs_screenshots/container_registry_docs.rb as a guide and as an example to create your own scripts.
The GitLab documentation is intended as the single source of truth (SSOT) for information about how to configure, use, and troubleshoot GitLab. The documentation contains use cases and usage instructions for every GitLab feature, organized by product area and subject. This includes topics and workflows that span multiple GitLab features and the use of GitLab with other applications.In addition to this page, the following resources can help you craft and contribute to documentation:
Style Guide - What belongs in the docs, language guidelines, Markdown standards to follow, links, and more.
Topic type template - Learn about the different types of topics.
Documentation process.
Markdown Guide - A reference for all Markdown syntax supported by GitLab.
Site architecture - How https://docs.gitlab.com is built.
Documentation for feature flags - How to write and update documentation for GitLab features deployed behind feature flags.Documentation for GitLab, GitLab Runner, GitLab Operator, Omnibus GitLab, and Charts is published to https://docs.gitlab.com. Documentation for GitLab is also published within the application at /help on the domain of the GitLab instance.
At /help, only help for your current edition and version is included. Help for other versions is available at https://docs.gitlab.com/archives/.The source of the documentation exists within the codebase of each GitLab application in the following repository locations:Documentation issues and merge requests are part of their respective repositories and all have the label Documentation.The CI pipeline for the main GitLab project is configured to automatically
run only the jobs that match the type of contribution. If your contribution contains
only documentation changes, then only documentation-related jobs run, and
the pipeline completes much faster than a code contribution.If you are submitting documentation-only changes to Omnibus or Charts,
the fast pipeline is not determined automatically. Instead, create branches for
docs-only merge requests using the following guide:Contributions to GitLab docs are welcome from the entire GitLab community.To ensure that the GitLab docs are current, there are special processes and responsibilities for all feature changes, that is development work that impacts the appearance, usage, or administration of a feature.However, anyone can contribute documentation improvements that are not associated with a feature change. For example, adding a new document on how to accomplish a use case that's already possible with GitLab or with third-party tools and GitLab.GitLab docs uses GitLab Kramdown
as its Markdown rendering engine. See the GitLab Markdown Guide for a complete Kramdown reference.Adhere to the Documentation Style Guide. If a style standard is missing, you are welcome to suggest one via a merge request.See the Folder structure page.To provide additional directives or useful information, we add metadata in YAML
format to the beginning of each product documentation page (YAML front matter).
All values are treated as strings and are only used for the
docs website.Each page should ideally have metadata related to the stage and group it
belongs to, as well as an information block as described below:
stage: The Stage
to which the majority of the page's content belongs.
stage: The Stage
to which the majority of the page's content belongs.
group: The Group
to which the majority of the page's content belongs.
group: The Group
to which the majority of the page's content belongs.
info: The following line, which provides direction to contributors regarding
how to contact the Technical Writer associated with the page's stage and
group:

To determine the technical writer assigned to the Stage/Group
associated with this page, see
https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments


info: The following line, which provides direction to contributors regarding
how to contact the Technical Writer associated with the page's stage and
group:
To determine the technical writer assigned to the Stage/Group
associated with this page, see
https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments

To determine the technical writer assigned to the Stage/Groupassociated with this page, seehttps://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignmentsFor example:
---
stage: Example Stage
group: Example Group
info: To determine the technical writer assigned to the Stage/Group associated with this page, see https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments
---

------stage: Example Stagestage:Example Stagegroup: Example Groupgroup:Example Groupinfo: To determine the technical writer assigned to the Stage/Group associated with this page, see https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignmentsinfo:To determine the technical writer assigned to the Stage/Group associated with this page, see https://about.gitlab.com/handbook/engineering/ux/technical-writing/#assignments------If you need help determining the correct stage, read Ask for help.The following metadata should be added when a page is moved to another location:
redirect_to: The relative path and filename (with an .md extension) of the
location to which visitors should be redirected for a moved page.
Learn more.
disqus_identifier: Identifier for Disqus commenting system. Used to keep
comments with a page that has been moved to a new URL.
Learn more.The docs website has comments (provided by Disqus)
enabled by default. In case you want to disable them (for example in index pages),
set it to false:
---
comments: false
---

------comments: falsecomments:false------Each page can have additional, optional metadata (set in the
default.html
Nanoc layout), which is displayed at the top of the page if defined.The type metadata parameter is deprecated but still exists in documentation
pages. You can safely remove the type metadata parameter and its values.NOTE:
This task is an MVC, and requires significant manual preparation of the output.
While the task can be time consuming, it is still faster than doing the work
entirely manually.It's important to keep the CODEOWNERS
file in the gitlab project up to date with the current Technical Writing team assignments.
This information is used in merge requests that contain documentation:To populate the eligible approvers section.By GitLab Bot to ping reviewers for community contributions.GitLab cannot automatically associate the stage and group metadata in our documentation
pages with the technical writer assigned to that group, so we use a Rake task to
generate entries for the CODEOWNERS file. Declaring code owners for pages reduces
the number of times GitLab Bot pings the entire Technical Writing team.The tw:codeowners Rake task, located in lib/tasks/gitlab/tw/codeowners.rake,
contains an array of groups and their assigned technical writer. This task:Outputs a line for each doc with metadata that matches a group in lib/tasks/gitlab/tw/codeowners.rake.
Files not matching a group are skipped.Adds the full path to the page, and the assigned technical writer.To prepare an update to the CODEOWNERS file:
Update lib/tasks/gitlab/tw/codeowners.rake with the latest TW team assignments.
Make this update in a standalone merge request, as it runs a long pipeline and
requires backend maintainer review. Make sure this is merged before you update
CODEOWNERS in another merge request.
Update lib/tasks/gitlab/tw/codeowners.rake with the latest TW team assignments.
Make this update in a standalone merge request, as it runs a long pipeline and
requires backend maintainer review. Make sure this is merged before you update
CODEOWNERS in another merge request.
Run the task from the root directory of the gitlab repository, and save the output in a file:

bundle exec rake tw:codeowners > ~/Desktop/updates.md


Run the task from the root directory of the gitlab repository, and save the output in a file:
bundle exec rake tw:codeowners > ~/Desktop/updates.md

bundle exec rake tw:codeowners > ~/Desktop/updates.mdbundleexecraketw:codeowners>~/Desktop/updates.md
Open the file you just created (~/Desktop/updates.md in this example), and prepare the output:

Find and replace ./ with /.
Sort the lines in alphabetical (ascending) order. If you use VS Code, you can
select everything, press F1, type sort, and select Sort lines (ascending, case insensitive.

Open the file you just created (~/Desktop/updates.md in this example), and prepare the output:Find and replace ./ with /.Sort the lines in alphabetical (ascending) order. If you use VS Code, you can
select everything, press F1, type sort, and select Sort lines (ascending, case insensitive.
Create a new branch for your CODEOWNERS updates.
Create a new branch for your CODEOWNERS updates.
Replace the documentation-related lines in the ^[Documentation Pages] section
with the output you prepared.
WARNING:
The documentation section is not the last section of the CODEOWNERS file. Don't
delete data that isn't ours!
Replace the documentation-related lines in the ^[Documentation Pages] section
with the output you prepared.WARNING:
The documentation section is not the last section of the CODEOWNERS file. Don't
delete data that isn't ours!
Create a commit with the raw changes.
Create a commit with the raw changes.
From the command line, run git diff master.
From the command line, run git diff master.
In the diff, look for directory-level assignments to manually restore to the
CODEOWNERS file. If all files in a single directory are assigned to the same
technical writer, we simplify these entries. Remove all the lines for the individual
files, and leave a single entry for the directory, for example: /doc/directory/ @tech.writer.
In the diff, look for directory-level assignments to manually restore to the
CODEOWNERS file. If all files in a single directory are assigned to the same
technical writer, we simplify these entries. Remove all the lines for the individual
files, and leave a single entry for the directory, for example: /doc/directory/ @tech.writer.
In the diff, look for changes that don't match your expectations:

New pages, or newly moved pages, show up as added lines.
Deleted pages, and pages that are now redirects, show up as deleted lines.
If you see an unusual number of changes to pages that all seem related,
check the metadata for the pages. A group might have been renamed and the Rake task
must be updated to match.

In the diff, look for changes that don't match your expectations:New pages, or newly moved pages, show up as added lines.Deleted pages, and pages that are now redirects, show up as deleted lines.If you see an unusual number of changes to pages that all seem related,
check the metadata for the pages. A group might have been renamed and the Rake task
must be updated to match.
Create another commit with your manual changes, and create a second merge request
with your changes to the CODEOWNERS file. Assign it to a technical writing manager for review.
Create another commit with your manual changes, and create a second merge request
with your changes to the CODEOWNERS file. Assign it to a technical writing manager for review.See redirects.Before getting started, make sure you read the introductory section
"contributing to docs" above and the
documentation workflow.Use the current merge request description template
Label the MR Documentation (can only be done by people with developer access, for example, GitLab team members)Assign the correct milestone per note below (can only be done by people with developer access, for example, GitLab team members)Documentation is merged if it is an improvement on existing content,
represents a good-faith effort to follow the template and style standards,
and is believed to be accurate.Further needs for what would make the doc even better should be immediately addressed
in a follow-up merge request or issue.If the release version you want to add the documentation to has already been
frozen or released, use the label ~"Pick into X.Y" to get it merged into
the correct release. Avoid picking into a past release as much as you can, as
it increases the work of the release managers.Every GitLab instance includes documentation at /help (https://gitlab.example.com/help)
that matches the version of the instance. For example, https://gitlab.com/help.The documentation available online at https://docs.gitlab.com is deployed every
four hours from the default branch of GitLab, Omnibus, Runner, and Charts.
After a merge request that updates documentation is merged, it is available online
in 4 hours or less.However, it's only available at /help on self-managed instances in the next released
version. The date an update is merged can impact which self-managed release the update
is present in.For example:A merge request in gitlab updates documentation. It has a milestone of 14.4,
with an expected release date of 2021-10-22.It is merged on 2021-10-19 and available online the same day at https://docs.gitlab.com.GitLab 14.4 is released on 2021-10-22, based on the gitlab codebase from 2021-10-18
(one day before the update was merged).The change shows up in the 14.5 self-managed release, due to missing the release cutoff
for 14.4.The exact cutoff date for each release is flexible, and can be sooner or later
than expected due to holidays, weekends or other events. In general, MRs merged
by the 17th should be present in the release on the 22nd, though it is not guaranteed.
If it is important that a documentation update is present in that month's release,
merge it as early as possible.When you're building a new feature, you may need to link to the documentation
from the GitLab application. This is normally done in files inside the
app/views/ directory, with the help of the help_page_path helper method.The help_page_path contains the path to the document you want to link to,
with the following conventions:It's relative to the doc/ directory in the GitLab repository.It omits the .md extension.It doesn't end with a forward slash (/).The help text follows the Pajamas guidelines.Use the following special cases depending on the context, ensuring all link text
is inside _() so it can be translated:
Linking to a doc page. In its most basic form, the HAML code to generate a
link to the /help page is:

= link_to _('Learn more.'), help_page_path('user/permissions'), target: '_blank', rel: 'noopener noreferrer'


Linking to a doc page. In its most basic form, the HAML code to generate a
link to the /help page is:
= link_to _('Learn more.'), help_page_path('user/permissions'), target: '_blank', rel: 'noopener noreferrer'

= link_to _('Learn more.'), help_page_path('user/permissions'), target: '_blank', rel: 'noopener noreferrer'=link_to_('Learn more.'),help_page_path('user/permissions'),target: '_blank',rel: 'noopener noreferrer'
Linking to an anchor link. Use anchor as part of the help_page_path
method:

= link_to _('Learn more.'), help_page_path('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'


Linking to an anchor link. Use anchor as part of the help_page_path
method:
= link_to _('Learn more.'), help_page_path('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'

= link_to _('Learn more.'), help_page_path('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'=link_to_('Learn more.'),help_page_path('user/permissions',anchor: 'anchor-link'),target: '_blank',rel: 'noopener noreferrer'
Using links inline of some text. First, define the link, and then use it. In
this example, link_start is the name of the variable that contains the
link:

- link_start = '<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe % { url: help_page_path('user/permissions') }
%p= _("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe % { link_start: link_start, link_end: '</a>'.html_safe }


Using links inline of some text. First, define the link, and then use it. In
this example, link_start is the name of the variable that contains the
link:
- link_start = '<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe % { url: help_page_path('user/permissions') }
%p= _("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe % { link_start: link_start, link_end: '</a>'.html_safe }

- link_start = '<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe % { url: help_page_path('user/permissions') }-link_start='<a href="%{url}" target="_blank" rel="noopener noreferrer">'.html_safe%{url: help_page_path('user/permissions')}%p= _("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe % { link_start: link_start, link_end: '</a>'.html_safe }%p=_("This is a text describing the option/feature in a sentence. %{link_start}Learn more.%{link_end}").html_safe%{link_start: link_start,link_end: '</a>'.html_safe}
Using a button link. Useful in places where text would be out of context with
the rest of the page layout:

= link_to _('Learn more.'), help_page_path('user/permissions'),  class: 'btn btn-info', target: '_blank', rel: 'noopener noreferrer'


Using a button link. Useful in places where text would be out of context with
the rest of the page layout:
= link_to _('Learn more.'), help_page_path('user/permissions'),  class: 'btn btn-info', target: '_blank', rel: 'noopener noreferrer'

= link_to _('Learn more.'), help_page_path('user/permissions'),  class: 'btn btn-info', target: '_blank', rel: 'noopener noreferrer'=link_to_('Learn more.'),help_page_path('user/permissions'),class: 'btn btn-info',target: '_blank',rel: 'noopener noreferrer'To link to the documentation from a JavaScript or a Vue component, use the helpPagePath function from help_page_helper.js:
import { helpPagePath } from '~/helpers/help_page_helper';

helpPagePath('user/permissions', { anchor: 'anchor-link' })
// evaluates to '/help/user/permissions#anchor-link' for GitLab.com

import { helpPagePath } from '~/helpers/help_page_helper';import{helpPagePath}from'~/helpers/help_page_helper';helpPagePath('user/permissions', { anchor: 'anchor-link' })helpPagePath('user/permissions',{anchor:'anchor-link'})// evaluates to '/help/user/permissions#anchor-link' for GitLab.com// evaluates to '/help/user/permissions#anchor-link' for GitLab.comThis is preferred over static paths, as the helper also works on instances installed under a relative URL.To link to the documentation from within Ruby code, use the following code block as a guide, ensuring all link text is inside _() so it can
be translated:
docs_link = link_to _('Learn more.'), help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }

docs_link = link_to _('Learn more.'), help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'docs_link=link_to_('Learn more.'),help_page_url('user/permissions',anchor: 'anchor-link'),target: '_blank',rel: 'noopener noreferrer'_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe%{docs_link: docs_link.html_safe}In cases where you need to generate a link from outside of views/helpers, where the link_to and help_page_url methods are not available, use the following code block
as a guide where the methods are fully qualified:
docs_link = ActionController::Base.helpers.link_to _('Learn more.'), Rails.application.routes.url_helpers.help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'
_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }

docs_link = ActionController::Base.helpers.link_to _('Learn more.'), Rails.application.routes.url_helpers.help_page_url('user/permissions', anchor: 'anchor-link'), target: '_blank', rel: 'noopener noreferrer'docs_link=ActionController::Base.helpers.link_to_('Learn more.'),Rails.application.routes.url_helpers.help_page_url('user/permissions',anchor: 'anchor-link'),target: '_blank',rel: 'noopener noreferrer'_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe % { docs_link: docs_link.html_safe }_('This is a text describing the option/feature in a sentence. %{docs_link}').html_safe%{docs_link: docs_link.html_safe}Do not use include ActionView::Helpers::UrlHelper just to make the link_to method available as you might see in some existing code. Read more in
issue 340567.Several RSpec tests
are run to ensure GitLab documentation renders and works correctly. In particular, that main docs landing page works correctly from /help.
For example, GitLab.com's /help.For information on how we build and deploy https://docs.gitlab.com, see Docs site architecture.See the Global navigation doc for information
on how the left-side navigation menu is built and updated.See how you can use review apps to preview your changes live.For more information about documentation testing, see the Documentation testing
guide.GitLab uses Danger for some elements in
code review. For docs changes in merge requests, whenever a change to files under /doc
is made, Danger Bot leaves a comment with further instructions about the documentation
process. This is configured in the Dangerfile in the GitLab repository under
/danger/documentation/.You can now set up an automatic screenshot generator to take and compress screenshots with the
help of a configuration file known as screenshot generator.To run the tool on an existing screenshot generator, take the following steps:Set up the GitLab Development Kit (GDK).Navigate to the subdirectory with your cloned GitLab repository, typically gdk/gitlab.Make sure that your GDK database is fully migrated: bin/rake db:migrate RAILS_ENV=development.Install pngquant, see the tool website for more information: pngquant
Run scripts/docs_screenshots.rb spec/docs_screenshots/<name_of_screenshot_generator>.rb <milestone-version>.Identify the location of the screenshots, based on the gitlab/doc location defined by the it parameter in your script.Commit the newly created screenshots.To add an additional screenshot generator, take the following steps:Locate the spec/docs_screenshots directory.Add a new file with a _docs.rb extension.Be sure to include the following bits in the file:
require 'spec_helper'

RSpec.describe '<What I am taking screenshots of>', :js do
  include DocsScreenshotHelpers # Helper that enables the screenshots taking mechanism

  before do
    page.driver.browser.manage.window.resize_to(1366, 1024) # length and width of the page
  end

require 'spec_helper'require'spec_helper'RSpec.describe '<What I am taking screenshots of>', :js doRSpec.describe'<What I am taking screenshots of>',:jsdo  include DocsScreenshotHelpers # Helper that enables the screenshots taking mechanismincludeDocsScreenshotHelpers# Helper that enables the screenshots taking mechanism  before dobeforedo    page.driver.browser.manage.window.resize_to(1366, 1024) # length and width of the pagepage.driver.browser.manage.window.resize_to(1366,1024)# length and width of the page  endendIn addition, every it block must include the path where the screenshot is saved
 it 'user/packages/container_registry/img/project_image_repositories_list'

 it 'user/packages/container_registry/img/project_image_repositories_list'it'user/packages/container_registry/img/project_image_repositories_list'To take a full page screenshot simply visit the page and perform any expectation on real content (to have capybara wait till the page is ready and not take a white screenshot).To have the screenshot focuses few more steps are needed:
find the area: screenshot_area = find('#js-registry-policies')

scroll the area in focus: scroll_to screenshot_area

wait for the content: expect(screenshot_area).to have_content 'Expiration interval'

set the crop area: set_crop_data(screenshot_area, 20)
In particular, set_crop_data accepts as arguments: a DOM element and a
padding. The padding is added around the element, enlarging the screenshot area.Please use spec/docs_screenshots/container_registry_docs.rb as a guide and as an example to create your own scripts.





