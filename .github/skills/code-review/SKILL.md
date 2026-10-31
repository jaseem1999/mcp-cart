---
name: code-review
description: Review code changes for correctness, regressions, security, and maintainability. Use when asked to review a pull request, diff, commit, or implementation.
---

# Code review

Review the requested changes as a reviewer. Do not edit files unless the user separately asks you to implement a fix.

## Review process

1. Establish the review scope from the user's request. For a working-tree review, inspect both staged and unstaged changes; for a commit or pull request, inspect that change set. Read surrounding code and relevant tests when needed to verify behavior.
2. Trace changed behavior through its callers, data flow, and error handling. Check boundary conditions, compatibility, persistence and concurrency implications, and whether the tests cover important behavior.
3. Prioritize concrete defects introduced by the changes: incorrect behavior, data loss, security or privacy issues, crashes, broken APIs, and missing validation. Report maintainability concerns only when they can plausibly cause a defect.
4. Verify each suspected issue against the code and available tests. Do not report hypothetical risks, style preferences, or pre-existing defects as findings. Do not claim tests were run unless you ran them.

## Finding criteria

- Each finding must identify a specific changed line or the smallest relevant line range and explain a reproducible failure scenario.
- Explain the impact and why the current behavior is incorrect; include the input, state, or calling condition when it matters.
- Rank findings by severity: **Critical** (widespread or severe impact, typically exploitable), **High** (serious defect likely to affect users or data), **Medium** (meaningful but limited defect), and **Low** (minor defect with a concrete consequence).
- Keep findings independent and concise. Avoid prescribing a fix unless it clarifies the issue.
- If no actionable issues are found, say so plainly and mention any relevant testing limitation.

## Repository context

This repository is a Java/Spring application. When relevant, check Spring configuration and dependency injection, HTTP/API behavior, authentication and authorization boundaries, JPA transaction and entity behavior, and compatibility with the project's configured Java and dependency versions. Follow existing project conventions rather than assuming a different framework or architecture.

## Response format

Lead with actionable findings, ordered by severity. For each finding, include:

`[Severity] path/to/file:line — Short title`

Then briefly describe the failure scenario and impact. Follow findings with a short note on tests run or limitations, if applicable. Do not include a general code summary unless the user asks for one.
