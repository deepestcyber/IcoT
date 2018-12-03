# IcoT
Intercraft of Things -- a Minecraft Mod to connect RL devices via mqtt to MC

Let MC world and the real world interact! This mod uses MQTT to publish redstone signals to the world and to subscribe
to real world signals as redstone.

This is currently a PoC (and the first MC modding I ever did). The basic concepts work, but the logic to support multiple 
signals is not included, yet. MQTT-broker cannot be configured. And update of redstone signals on change does not work.
This is currently a mixture of different states of concepts for MC-forge.
