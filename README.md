# Dinner - A LibGDX Detective Adventure

Welcome to **Dinner**, a mystery/investigation game built with [LibGDX](https://libgdx.com/) where you play as a detective seeking to uncover secrets lurking within a mysterious manor. This project was born from a passion for story-driven experiences and was carefully developed by our team using an Agile approach, emphasizing collaboration, iterative improvements, and code quality.

---

## Table of Contents
1. [Story Overview](#story-overview)  
3. [Technologies & Frameworks](#technologies--frameworks)  
4. [Project Architecture](#project-architecture)  

---

## Story Overview

Set in the heart of the Tuscan countryside, **Dinner** invites players into a gripping mystery where past and present intertwine. You step into the shoes of a seasoned investigator, drawn to an opulent yet enigmatic estate under unusual circumstances. The gathering at the manor, hosted by an elusive figure, is shrouded in secrecy, and tensions rise as the night unfolds.

As you explore the grand halls and hidden corners of the estate, every conversation, object, and clue becomes a piece of a larger puzzle. The walls of the manor whisper with forgotten stories, and the guests all seem to carry secrets of their own. It’s up to you to navigate the intrigue, uncover the truth, and determine who—or what—can be trusted.

---

## Technologies & Frameworks

- **Java & LibGDX**: Core game framework for rendering, input handling, and cross-platform builds.
- **Gradle**: Build automation and dependency management.
- **Tiled**: Used to design tile-based maps ( `.tmx` ), enabling structured environments.
- **JUnit & Mockito**: Comprehensive testing of logic and interactions.
- **Aseprite**: Creation of pixel-art assets and animations.
- **GarageBand** (or similar DAW): Composition and editing of music and sound effects.
- **Jira**: Used to plan sprints, user stories, tasks, and keep an Agile workflow.

---

## Project Architecture

We adopted a **Model-View-Controller (MVC)** architecture to keep domain logic separate from rendering and user interaction. Key layers include:

- **Model**: Holds all game logic and data structures (e.g., `Player`, `NPC`, `Items`, `Notebook`, etc.).  
- **View**: Renders visuals and UI, including sprites, tilesets, and HUD components.  
- **Controller**: Interprets input from the player, triggers updates in the Model, and refreshes the View accordingly.

In addition, we employed several **design patterns**:
- **Singleton**: Ensures unique instances for classes like `Player`.  
- **Facade**: Simplifies complex operations for interactable objects (e.g., `InteractableObjectController`).  
- **State**: Manages transitions between menus, screens, and game states.  
- **Template Method**: Streamlines interactions with environment objects (abstract behaviors vs. specialized overrides).  
- **Memento**: Safely captures & restores game states (used in `SaveController` to handle saves).

---

Thank you for checking out Dinner!
