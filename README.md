OllyCode
========

The world's easiest programming language.

Defining a function:

    define <NAME> using <VARS (var1, var2)> {
      give var1 to var2
    }
`Example`:

    define give using item, player {
      player.getInventory().addItem(item)
    }
    
`Example usage:`

    give item to player


Defining listener:

    when <object> <event> <extra variables> {
      //Code
    }
`Example usage:`

    when player clicks block {
      give Item(STONE) to player
    }
    
_Thanks molen!_
