# Reflection

## 1. Which AI tools or models did you use?
I exclusively used Gemini Pro for this project. To get the most out of it, I assigned it distinct personas (such as Architect Agent, Implementation Agent, and Testing/Reviewer Agent) depending on the phase of development. This helped constrain the AI's focus so it wouldn't try to write the entire application when I only wanted design feedback.

## 2. Which prompt was the most useful? Why?
Prompt 16 was by far the most useful. I was struggling with `ConcurrentModificationException` crashes, and the Implementation Agent initially told me to wrap all my loops in `synchronized` blocks. I felt this would cause performance bottlenecks, so I proposed an alternative—defensive array copying inside the getters—and asked the Reviewer Agent to evaluate it. The Reviewer Agent broke down exactly why my idea was superior for maintainability and thread safety. As a student, concurrency is incredibly intimidating; having the AI validate my architectural intuition over its own previous suggestion was a massive confidence builder. 

## 3. Which AI-generated suggestion was wrong, incomplete, or misleading?
The AI sounded incredibly authoritative when it gave me a completely broken fix for date-parsing in `FileStorageService.java` (Prompt 13). It told me to replace my date logic with a strict string pattern formatter, which ended up crashing the system during file reads. Furthermore, in Prompt 28, it completely ignored my constraints and generated an entire GUI framework in JavaFX, which I didn't know how to use. It taught me a hard lesson: AI can be confidently wrong, and blindly pasting code you don't understand is a recipe for disaster.

## 4. How did you check whether AI-generated code was correct?
I relied heavily on iterative, manual verification. Instead of letting the AI write the whole file, I had it generate small blocks (like the math formulas in `CombatSimulatorService`) and then hand-coded them into my project line-by-line. When debugging, I fed the AI targeted batches of test results (Prompt 26) rather than the whole codebase. I also made it a rule to mentally step through the logic of the AI's code before hitting "Run," which caught several null-pointer risks before they ever compiled.

## 5. What bugs did you fix yourself instead of asking AI to fix?
I manually reverted the date-parsing system crash that the AI introduced. I also had to manually tweak the initial dataset in `DataInitializer.java` because the AI completely misunderstood the assignment's association requirements regarding how heroes should be assigned to players. Most proudly, I manually fixed a massive menu fall-through bug in `Main.java` by tracking down missing `break` statements in a nested `switch` block. Fixing that without an LLM crutch proved to me that my foundational debugging skills were actually improving.

## 6. What Java concept did you understand better after using AI?
I gained a profound understanding of heap memory references versus object mutation. During the "Ghost Object" update flaw (Prompt 19), I was replacing an updated player in my list using `.set(i, updatedPlayer)`. The AI pointed out that doing this overwrites the underlying memory reference, instantly breaking the link to any team rosters that still held the *old* pointer. The AI taught me to locate the existing object and mutate its fields directly using setters. That single interaction bridged the gap between procedural programming and true Object-Oriented memory management for me.

## 7. What Java concept are you still unsure about?
I am still a bit unsure about the deeper, under-the-hood mechanics of Java File I/O. The AI successfully generated the `BufferedReader` and `PrintWriter` boilerplate for my CSV serialization, and I understand the high-level concept of writing lines to a file. However, the precise management of byte streams, buffer flushing, and character escaping still feels a bit like a black box. I want to study this further so I don't have to rely on AI to parse complex text files in the future.

## 8. Did AI make the project easier, harder, or both? Explain.
It definitely did both. It made the project drastically easier by eliminating the friction of typing out tedious boilerplate. Setting up the Swing UI terminal interceptor and writing out the probability math for the combat simulator would have taken me hours, but the AI did it in seconds. However, it made the project harder when I had to untangle its bad architectural advice. Untangling the flawed JavaFX pivot and fixing the ghost object bugs required intense mental effort. AI accelerates typing, but it can actually slow down your architectural thinking if you aren't careful.

## 9. Which parts of the final project were mainly written by you?
I was strictly responsible for the core architectural routing and class design choices. I manually built the decoupled `SearchService` because the AI tried to dangerously cram it into the data manager. I also designed the layout logic for the Swing panels, the custom associations in the mock data, and the overarching decision to use snapshot defensive copying for thread safety. I made sure I acted as the lead architect so I would never feel like a tourist in my own codebase.

## 10. Which parts were mainly generated or heavily assisted by AI?
I essentially used the AI as an extremely fast junior developer, offloading the heavy, algorithmic lifting to it. The CSV serialization parsing algorithms, the complex randomized math inside the turn-based combat simulator, and the highly specific asynchronous thread-hijacking needed to route `System.out` logs into the Swing GUI `JTextArea` were all heavily generated by Gemini.