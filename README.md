LibSchematic
============

LibSchematic is a library for Forge (http://minecraftforge.net/) to allow mods to import
.schematic files into a world, as part of worldgen, multiblock structure validation or misc.
usage.

Features
-----------
* Extended block storage, compatible with MCEdit 0.17 and WorldEdit schematics.
* Deployed as a mod, (like CodeChickenCore) for all mods to hook into and use.
* Load and process .schematic files
* Paste .schematic file into world.
* Validate area of world matches a schematic.
* API to extend LibSchematic.

API
----
LibSchematic exposes an API for mod developers to both use LibSchematic and ensure their
blocks are compatible.

There are three major parts of the API are workers, SchematicDataHandlers and extensions.

Workers are objects that manipulate the data being pulled from a schematic during a paste.
They can do various things like transform the block id/metadata on the fly (such as during
rotation, or to "colourize" a team's base from a generic schematic), cancel a block change
or modify tileEntities.

SchematicDataHandler is a set of interfaces used by the built in workers to manipulate the
data of blocks. They are used to calculate the new metadata for a block when a schematic is
rotated. Mod authors can provide a SchematicDataHandler for their mod blocks, telling
LibSchematic how to rotate a particular block. Work is also been done on a handler to
change the owner of a block, allowing pastes of schematics containing private blocks.

Extensions are a way to load and store additional, non-standard data with a schematic.
LibSchematic ships with two of these WorldEdit Vectors and Layers.

WorldEdit Vectors provides access to the WEOrigin and WEOffset vectors added by WorldEdit.
This extension is used by the OffsetWorker to paste a schematic by it's offset, rather than
a corner. This can for some schematics be more intuitive.

Layers is a work in progress extension by me. Layers adds an extra value to each block in
the schematic, it's layer Id. A LayerWorker can be told to only paste specific layers,
providing a much more fine tuned way to only paste what you want.

Example uses are to be able to paste a complex structure underground without air pockets
outside the structure or filling air blocks inside the structure, randomising a schematic
by the addition/subtraction of sections (such as the decay of an old church).

