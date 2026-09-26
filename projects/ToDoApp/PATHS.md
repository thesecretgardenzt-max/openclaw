# ToDoApp project paths

Active project root:

`workspace-shared/projects/ToDoApp/`

Use this root for all ToDoApp inputs, outputs, code, artifacts, and evidence.

## Current folders

- Product brief: `workspace-shared/projects/ToDoApp/product-brief/`
- Requirements: `workspace-shared/projects/ToDoApp/requirements/`
- Architecture: `workspace-shared/projects/ToDoApp/architecture/`
- Implementation: `workspace-shared/projects/ToDoApp/implementation/`
- App code: `workspace-shared/projects/ToDoApp/app/`
- QA: `workspace-shared/projects/ToDoApp/qa/`
- DevOps: `workspace-shared/projects/ToDoApp/devops/`

Do not write new ToDoApp artifacts directly under root-level `workspace-shared/app`, `workspace-shared/requirements`, `workspace-shared/architecture`, `workspace-shared/implementation`, `workspace-shared/qa`, `workspace-shared/devops`, or `workspace-shared/product-brief`.

## Validation

- Legacy path reference scan for active config/instructions/tasks/ToDoApp artifacts: `0` findings.
- App test from new path: `cd workspace-shared/projects/ToDoApp/app && .venv/bin/python -m pytest -q` → `9 passed`.
