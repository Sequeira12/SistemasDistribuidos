

tell application "Terminal"

set position of front window to {0, 0}
set default settings to settings set "Homebrew"
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.QueueUrls"
set default settings to settings set "Homebrew"

set position of front window to {950, 0}
delay (1)
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.MessageServerInterfaceServer"


delay (1)
set position of front window to {0, 300}
set default settings to settings set "Homebrew"
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.Downloaders 9999"


delay (1)
set position of front window to {950, 300}
set default settings to settings set "Homebrew"
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.Downloaders 9998"
delay (1)

set position of front window to {455, 300}
set default settings to settings set "Homebrew"
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.Downloaders 9997"
delay (1)
set position of front window to {0, 600}
set default settings to settings set "Homebrew"
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.Barrels 4445"
delay (1)

set default settings to settings set "Homebrew"
set position of front window to {950, 600}
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.Barrels 4448"

delay (1)
set position of front window to {455, 600}
set default settings to settings set "Homebrew"
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.Barrels 4444"

delay (1)
set position of front window to {455, 0}
set default settings to settings set "Homebrew"
do script "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java  -classpath /Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/out/production/PROJETOSDEASYYYY:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/jsoup-1.15.4.jar:/Users/brunosequeira/IdeaProjects/PROJETOSDEASYYYY/SistemasDistribuidos/postgresql-42.5.4.jar Message.MessageServerInterfaceClient"

end tell
