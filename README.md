# SYM - Labo 3 - Environnement I (_Code-barres, iBeacons et NFC_)

> Auteurs: Adrien Peguiron, Noémie Plancherel, Nicolas Viotti
>
> Date: 19.12.2021

## 2. Balises NFC

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


### 2.4 Questions

#### 2.4.1

> Dans la manipulation ci-dessus, les tags NFC utilisés contiennent 4 valeurs textuelles codées en UTF-8 dans un format de message NDEF. Une personne malveillante ayant accès au porte-clés peut aisément copier les valeurs stockées dans celui-ci et les répliquer sur une autre puce NFC.
>
> A partir de l’API Android concernant les tags NFC3, pouvez-vous imaginer une autre approche pour rendre plus compliqué le clonage des tags NFC? Existe-il des limitations? Voyez-vous d’autres possibilités?

La présence d'authentification à multiples facteurs donne à l'application une certaine sécurité même si les valeurs de la puce NFC ont été clonées. En effet, l'utilisateur malveillant doit également connaître la paire `username` `mot de passe`, ou avoir accès à une application où le login a déjà été effectué.

La plupart des tags ont un id unique non modifiable. Celui-ci pourrait être utilisé pour spécifié quel tag contient les données et éviter qu'un autre tag contenant des données clonées fonctionnent.

Mais certains tag ont une id modifiable ce qui permettrait à l'attaquant d'avoir le même id de tag que l'original et contourner cette protection.

Les tags NFCs ne sont pas adaptés à l'authentification de part leur facilité à être clonés, il est préférable d'utiliser d'autres moyens plus sécurisés.

Il existe également des smartcards sans contact qui fonctionnent de manière similaire au NFC et permettent d'accéder aux données à l'aide d'une clé asymétrique. Cette manière de procéder donne une couche de sécurité supplémentaire non négligeable.


#### 2.4.2

> Est-ce qu’une solution basée sur la vérification de la présence d’un iBeacon sur l’utilisateur, par exemple sous la forme d’un porte-clés serait préférable? Veuillez en discuter.


Les tags NFC peuvent facilement être clonés mais cela nécessite d'avoir un accès direct à la puce NFC car la portée de celles-ci est très faible. Les iBeacon ayant une portée bien plus grande, le clonage est alors grandement facilité sans avoir besoin d'un accès physique direct.

Il serait également possible de s'authentifier avec l'iBeacon d'une personne se trouvant à proximité sans que celle-ci ne réalise.

## 3. Codes-barres

### 3.2 Questions

#### 3.2.1

> Quelle est la quantité maximale de données pouvant être stockée sur un QR-code? Veuillez expérimenter, avec le générateur conseillé de codes-barres (QR), de générer différentes tailles de QR-codes. Pensez-vous qu’il est envisageable d’utiliser confortablement des QR-codes complexes  (par  exemple  du  contenant  >500  caractères  de  texte,une  vCard très complète ou encore un certificat Covid)?

Les QR-code sont composés de modules(point noir et blanc qui composent le QR), dont le nombre/la configuration varient en fonction de la version du QR-code. Il existe 40 versions différentes et la "configuration du module" dépend en réalité du nombre de modules présents dans le QR. Plus on souhaite ajouter de données, plus on doit augmenter le nombre de modules et donc par conséquent, la taille (physique) du qr-code.
Ainsi on peut lire que le plus de données peut être stocké dans un QR-code Version 40 (177x177 modules) avec un CCM (Data correction level) Low. Cela permet alors de stocker 7089 caractères numériques ou 4296 caractères alphanumériques.

En pratique maintenant, en se référant au site fourni dans la consigne, on remarque qu'un texte de 500 caractères ne pose pas de problèmes peu importe l'encodage ou le niveau de correction qu'on décide d'appliquer.
Il en va de même pour la vCard, à condition de ne pas surcharger les champs, notamment le champ "Notes".


#### 3.2.2

> Il existe de très nombreux services sur Internet permettant de générer des QR-codes dynamiques. Veuillez expliquer ce que sont les QR-codes dynamiques. Quels sont les avantages et  respectivement  les inconvénients à utiliser ceux-ci en comparaison avec des QR-codes statiques.Vous adapterez votre réponse à une utilisation depuis une plateforme mobile.

Un QR code dynamique est un QR-code qui contient en réalité une url raccourcie. L'information qu'on essaye d'atteindre n'est en réalité pas contenue dans le QR-code lui-même. L'information est contenue sur un site web et l'url nous y redirige. L'url de redirection peut donc changer, contrairement à un QR-code statique.
Les avantages d'un QR-Code dynamique :
1. Le contenu peut changer au fil du temps. Si on prend l'exemple du menu d'un restaurant, on peut s'attendre à ce que ce menu change au fur et à mesures des semaines. Grâce au QR dynamique, on peut alors changer le menu sans avoir à changer le QR-code.
2. On peut traquer l'utilisation du qr-code. Lorsqu'un code est scanné, on peut capturer où, quand et avec quel appareil le scan a été effectué. Ces statistiques peuvent alors être répertoriées et utilisées à des fins commerciales, marketing ou politiques.
3. Etant donné les paramètres récupérées au point 2, on peut alors personnaliser de manière dynamique le contenu du qr. Pour reprendre l'exemple du restaurant, en fonction de l'heure à laquelle le scan a été effectué, on peut imaginer afficher le menu du midi ou du soir.
4. Les codes dynamiques sont particulièrement adaptés aux payements et commandes en ligne depuis un mobile. Imaginons une commande à une table de restaurant, en scannant un QR-code, l'URL nous envoie sur un site web développé pour les mobiles, où on peut ajouter les articles à notre panier directement. Envoyer la commande génère alors un code pour le serveur à scanner ou envoie directement la commande. Les informations nécessaires à ce genre de manipulation est apparemment trop compliqué à enregistrer dans un code statique.
5. Les codes peuvent être activé/désactivé à volonté par l'entité qui l'a généré, en redirigeant l'URL vers une page d'erreur par exemple. On peut imaginer qu'après une centaine de scans, le code qr redirige la personne ayant scanné vers une autre adresse.
6. Etant donné le point 2 encore, on peut imaginer qu'on souhaite rediriger différemment les différents appareils mobiles en fonction de leur OS. Ainsi, en scannant le code, le QR peut détecter l'OS utilisé et rediriger vers le bon Store en fonction.

## 4. Balises iBeacon

### 4.2 Question

> Les iBeacons sont très souvent présentés comme une alternative à NFC. Vous commenterez cette affirmation en vous basant sur 2-3 exemples de cas d’utilisations (use-cases) concrets (par exemple e-paiement, second facteur d’identification, accéder aux horaires à un arrêt de bus, etc.)

Les iBeacons sont des balises sans fil qui émettent des messages en Bluetooth, développés par Apple. Cette technologie sans contact propose quelques avantages comme:

- Une portée pouvant aller jusqu'à 70 mètres environ (contre ~10 centimètres pour NFC)
- Vu qu'elle fonctionne avec la technologie BLE - _bluetooth low energy_, les iBeacons peuvent fonctionner plusieurs années avec la même batterie

Un des désavantages qu'elle présente serait qu'elle peut collecter des informations privées, ce qui peut présenter un problème de sécurité pour certaines entreprises.

Voici quelques exemples d'utilisations:

1. Dans un cas où on serait dans un **aéroport** et qu'on souhaiterait recevoir des informations sur notre vol lorsqu'on attend à la gate par exemple, il serait plus approprié d'utiliser des iBeacons afin d'éviter de devoir aller scanner un tag NFC. De plus, un iBeacon permet d'émettre des informations auprès plusieurs clients en même temps, ce qu'il serait également adapté pour cette situation.

2. Pour un **paiement sans contact**, la technologie NFC est bien mieux adapatée car elle fonctionne mieux avec des portées courtes (quelques centimètres). Comme dit précédemment, un iBeacon émet des informations jusqu'à une grande portée, 70 mètres, ce qui pourrait permettre à un attaquant de faire une attaque de type spoof et d'effectuer des paiements non-voulus. Vu que NFC a été spécialement designé pour les paiements, elle offre des fonctions de chiffrement, ce qui permet de garantir la confidentialité des données.

3. Dans un cas où on serait dans un **magasin**, si on souhaite pouvoir envoyer des offres promotionnels ou des conseils sur certains produits lorsque le client se trouve dans l'allée concernée, il serait plus intéressant d'utiliser des iBeacons, car comme pour le premier cas, il est possible d'émettre ses informations à plusieurs utilisateurs.

En conlusion, nous constatons que iBeacon n'est pas une alternative directe à NFC, c'est-à-dire que tout dépend du cas d'utilisation. Lors de transmissions de données sensibles, tel qu'un paiement sans-contact, il est préférable d'utiliser des tags NFC. Cependant, si on souhaite envoyer des informations à plusieurs utilisateurs dans un grand périmètre, on peut utiliser des iBeacons.

