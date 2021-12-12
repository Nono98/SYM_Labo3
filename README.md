# SYM - Labo 3 - Environnement I (_Code-barres, iBeacons et NFC_)

> Auteurs: Adrien Peguiron, Noémie Plancherel, Nicolas Viotti
>
> Date: 19.12.2021

## 2. Balises NFC

### 2.4 Questions

#### 2.4.1

> Dans la manipulation ci-dessus, les tags NFC utilisés contiennent 4 valeurs textuelles codées en UTF-8 dans un format de message NDEF. Une personne malveillante ayant accès au porte-clés peut aisément copier les valeurs stockées dans celui-ci et les répliquer sur une autre puce NFC.
>
> A partir de l’API Android concernant les tags NFC3, pouvez-vous imaginer une autre approche pour rendre plus compliqué le clonage des tags NFC? Existe-il des limitations? Voyez-vous d’autres possibilités?

#### 2.4.2

> Est-ce qu’une solution basée sur la vérification de la présence d’un iBeacon sur l’utilisateur, par exemple sous la forme d’un porte-clés serait préférable? Veuillez en discuter.

## 3. Codes-barres

### 3.2 Questions

#### 3.2.1

> Quelle est la quantité maximale de données pouvant être stockée sur un QR-code? Veuillez expérimenter, avec le générateur conseillé de codes-barres (QR), de générer différentes tailles de QR-codes. Pensez-vous qu’il est envisageable d’utiliser confortablement des QR-codes complexes  (par  exemple  du  contenant  >500  caractères  de  texte,une  vCard très complète ou encore un certificat Covid)?



#### 3.2.2

> Il existe de très nombreux services sur Internet permettant de générer des QR-codes dynamiques. Veuillez expliquer ce que sont les QR-codes dynamiques. Quels sont les avantages et  respectivement  les inconvénients à utiliser ceux-ci en comparaison avec des QR-codes statiques.Vous adapterez votre réponse à une utilisation depuis une plateforme mobile.

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

