---
name: pr-creation
description: Prepare and create a GitHub pull request for the current changes, including reviewing the diff, validating changes, and writing a focused title and description.
---

# Pull request creation

Create a pull request for the user's requested changes. Preserve unrelated work and do not claim a PR was created unless the hosting service confirms it.

## Process

1. Inspect the repository state, current branch, remotes, and the diff against the likely base branch. Review staged, unstaged, and untracked files. Do not overwrite or discard existing changes.
2. Confirm the change is complete and focused. Run the smallest relevant tests or checks available; report failures and limitations accurately. Do not include unrelated changes in the PR.
3. Determine the target base branch from repository conventions and remote state. If the target is ambiguous and materially affects the PR, ask the user.
4. Prepare a concise title and description that explain the motivation, user-visible changes, and validation performed. Include relevant issue references only when supported by repository context or the user's request.
5. If changes are not already on a suitable remote branch, create or use a focused branch, commit only the requested changes, and push it. Never amend commits or rewrite shared history. If existing user changes make safe staging unclear, ask before committing.
6. Create the pull request using the available authorized GitHub integration or GitHub CLI. Use the chosen base and head branches, and use draft status only when requested or clearly indicated by the repository workflow.
7. Verify the created PR's URL, title, base, and head from the service response, then report the URL and any validation limitations.

## Safety and scope

- Never force-push, delete branches, or change repository settings as part of PR creation.
- Do not stage, commit, or publish unrelated user changes.
- Do not expose credentials or copy secrets into the PR title, body, comments, or logs.
- If authentication, permissions, or a required target branch prevents creation, stop and explain the blocker rather than implying success.
