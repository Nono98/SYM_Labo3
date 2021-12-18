# SYM_Labo3

### 2. Balises NFC

#### 2.2 Manipulation

##### Choix d'implémentation :

Avant de tenter d'accéder aux différentes ressources, l'utilisateur devra se connecter à l'aide d'une paire `username` `password`. Dans le cadre de ce laboratoire, cette paire est codée en dur dans le code. C'est une pratique à éviter en temps normal mais le but de cette manipulation était de se familiarisé avec les balises NFC et le login a été mis en second plan. 

`username` : `user1`

`password` : `mdpUser1!`

Une fois l'utilisateur connecté, il doit scanner une balise NFC avant de pouvoir utiliser les différents boutons.

Lors du scan, l'application vérifie si le premier message NFC contient la chaîne de caractères `test`.

Si c'est le cas, le niveau d'authentification de l'utilisateur est défini à 15. Toutes les 10 secondes, il perd 1 niveau d'authentification. 

Les 3 boutons à disposition son accessibles en fonction du niveau d'authentification. Le premier nécessite au moins 15, le deuxième 10 et le dernier 5. 

Si l'utilisateur scanne à nouveau la balise NFC, son niveau d'authentification remonte à 15.

Des messages sous forme de `toast` sont affichés pour indiquer la réussite ou échec du login et de l'accès aux boutons.

#### 2.4 Question

##### 2.4.1 

> Dans la manipulation ci-dessus, les tags NFC utilisés contiennent 4 valeurs textuelles codées en UTF-8 dans un format de message NDEF. Une personne malveillante ayant accès au porte-clés peut aisément copier les valeurs stockées dans celui-ci et les répliquer sur une autre puce NFC.
>
> A partir de l’API Android concernant les tags NFC3, pouvez-vous imaginer une autre approche pour rendre plus compliqué le clonage des tags NFC? Existe-il des limitations? Voyez-vous d’autres possibilités?

La présence d'authentification à multiples facteurs donne à l'application une certaine sécurité même si les valeurs de la puce NFC ont été clonées. En effet, l'utilisateur malveillant doit également connaître la paire `username` `mot de passe`, ou avoir accès à une application où le login a déjà été effectué.

La plupart des tags ont un id unique non modifiable. Celui-ci pourrait être utilisé pour spécifié quel tag contient les données et éviter qu'un autre tag contenant des données clonées fonctionnent.

Mais certains tag ont une id modifiable ce qui permettrait à l'attaquant d'avoir le même id de tag que l'original et contourner cette protection.

Les tags NFCs ne sont pas adaptés à l'authentification de part leur facilité à être clonés, il est préférable d'utiliser d'autres moyens plus sécurisés.

Il existe également des smartcards sans contact qui fonctionnent de manière similaire au NFC et permettent d'accéder aux données à l'aide d'une clé asymétrique. Cette manière de procéder donne une couche de sécurité supplémentaire non négligeable.

##### 2.4.2

> Est-ce qu’une solution basée sur la vérification de la présence d’un iBeacon sur l’utilisateur, par exemple sous la forme d’un porte-clés serait préférable? Veuillez en discuter.

Les tags NFC peuvent facilement être clonés mais cela nécessite d'avoir un accès direct à la puce NFC car la portée de celles-ci est très faible. Les iBeacon ayant une portée bien plus grande, le clonage est alors grandement facilité sans avoir besoin d'un accès physique direct.

Il serait également possible de s'authentifier avec l'iBeacon d'une personne se trouvant à proximité sans que celle-ci ne réalise.