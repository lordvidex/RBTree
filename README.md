## RES FOLDERS

### CSV FILES

Excel files generated from Main.java showing **timeInNanoSeconds, number of iterations and itemCount** during the
specified operations

- `analysis_delete.csv`
- `analysis_find.csv`
- `analysis_insert.csv`

### TESTCASE

`test_case.txt` file contains test cases generated
from [TCGenerator.java](https://github.com/lordvidex/RBTree/tree/main/src/TCGenerator.java)

### PRETTY_TREE

Run the command in terminal to view `pretty_tree.txt` properly  

*Linux/Unix based OS*
>> **$ less -R res/pretty_tree.txt**  

*Windows OS*
> > **> more res/pretty_tree.txt** 

**PS:** if `more` does not work on Windows, 
- use bash with the linux command
- or any ANSI decodable editor e.g. Sublime Text, Atom, etc
- or read file into terminal (`System.out.println(tree)`) has the same effect.

## SRC FILES
- **Main.java**   
    Entry point of program
- **RBTree.java**   
    Java RedBlack Tree class
- **SpeedAnalysis**  
    Class for reading time and iteration results to `csv` files
- **Node.java**  
    Contains Node definition and it's helper functions
- **TCGenerator**  
    Class for generating test cases
  
## SOURCES AND CREDITS
Red Black Tree implementation was built upon [RBTREE on Programiz](https://www.programiz.com/dsa/deletion-from-a-red-black-tree)  
Tree prettifier was built upon [Baeldung's Article on printing Binary trees](https://www.baeldung.com/java-print-binary-tree-diagram)