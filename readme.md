# KANSEN at war

## What is this

This is a naval war sim game, but cointains no scripts, only progame. My original intention is to design a hardcore game based on the settings and cute girls in game Azurlane, but it shuold not be too difficult to create a scripts based on simliar games. In order to avoid charges on copyrights and save space, there is no content from the game here in this repository, but I will provide a small programe to convert the asserts into the resources the game can use. It is now far from playable, and there is no guarantee that this project can be finished. Any help is welcome and I'll try my best to make these code easy to maintance.

## Other important stuff about gameplay

TBD

## Project Structure

| Sub-forder | Description | Status | Language |
| --- | --- | --- | --- |
| `client` | The client of the game, a simple server <br> manager is provided | planning | Qt C++ |
| `compiler` | The tool for compiling a script | planning | Not decided, <br> probably C++ |
| `extracter` | The tool for extracting resources from <br> the original game files (also a walk <br> around of copyright charges) | planning | Depends on <br> different game |
| `scriptingdoc` | Documents for scripters, the manual <br> for the whole project | writting | markdown |
| `scriptingtool` | Some small tools for calculating | writting | python? |
| `server` | The server of the game, can not be manager <br> directly. | writting | java |

We may provide a `servermanager` in the future for better and advanced management of servers.

If you think the progess is too slow, fork and make pull requests!
