CC = javac
RN = java
DOC = javadoc

DFLAG = -d
CPFLAG = -cp
RM = rm -rf
MK = mkdir
MAIN = Main

CLSPATH = build
DOCPATH = doc
DOCPACKAGE = src.cube src.cube3X3 src.solve3X3 src.cube3X3GUI
HTMLVERSION = -html5

SRC = src/cube/RubiksCubeInterface.java\
src/cube/RubiksCubeException.java\
src/cube/RubiksCube.java\
src/cube3X3/RubiksCube3X3.java\
src/cube3X3/RubiksCube3X3Factory.java\
src/cube3X3GUI/MainFrame3X3.java\
src/solve3X3/Algorithm3X3.java\
src/solve3X3/PlusBottom.java\
src/solve3X3/LayerFirst.java\
src/solve3X3/LayerSecond.java\
src/solve3X3/PlusTop.java\
src/solve3X3/AlignCenter.java\
src/solve3X3/Corner.java\
src/solve3X3/LayerThird.java\
src/solve3X3/Solve3X3.java\
src/solve3X3/Optimizer.java\
Main.java\


all: compile run doc

compile:
	$(CC) $(CPFLAG) $(CLSPATH) $(SRC) $(DFLAG) $(CLSPATH)

run:
	$(RN) $(CPFLAG) $(CLSPATH) $(MAIN)	

doc:
	$(DOC) $(DFLAG) $(DOCPATH) $(DOCPACKAGE) $(HTMLVERSION)

clean: 
	$(RM) $(CLSPATH) $(DOCPATH)  
