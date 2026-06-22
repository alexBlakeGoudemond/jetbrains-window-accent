# AI Development Guidelines

This repository uses a centralized AI playbook.

To speed up Agent / AI Model onboarding, please refer to the directory `.ai-resources`.
Several resources are available here. In particular:
- [window-accent-overview.md](.ai-resources/repository-information/window-accent-overview.md) for a high-level overview of the codebases in this repository.

Primary source of truth:

- .ai-playbook/instructions/AGENTS.md
- .ai-playbook/instructions/CONVENTIONS.md
- .ai-playbook/instructions/CONTEXT.md
- .ai-playbook/prompts/ contains available prompts that the user may invoke

The following resources should also exist, and are designed to assist the AI Tool:
- .ai-playbook/skills/ 
- .ai-playbook/workflows/ 
- .ai-playbook/guardrails/ 

> NOTE - subfolders may exist, for example `.ai-playbook/prompts/logs/*`

Follow these rules when generating or modifying code:

- Prefer patterns defined in the AI playbook
- Maintain consistency with existing architecture
- Use repository conventions over generic suggestions
