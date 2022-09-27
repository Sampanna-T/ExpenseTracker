cc = javac
cp = -cp
rn = java
dc = javadoc
d = -d
Main = Main
rm = rm -rf
del = rm -rf

src = src/MainFrame.java\
Main.java

dotClassFile1 = *.class
dotClassFile2 = src/*.class
clsPath = "D:\MyInstalls\MySQLJar\mysql-connector-java-8.0.30\mysql-connector-java-8.0.30"
clasPath2 = build
docPath = doc

docPackage = src
htmlVersion = -html5

all: clean build doc run

build:
	$(cc)  $(cp) $(clsPath1);$(clasPath2) $(src) $(d) $(clsPath)

run:
	$(rn) $(cp) $(clsPath);$(clasPath2) $(Main)

doc:
	$(dc) $(d) $(docPath) $(docPackage) $(htmlVersion)	

clean:
	$(del) $(dotClassFile1)
	$(del) $(dotClassFile2)

