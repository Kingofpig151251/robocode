db.players.insertMany([
	{playerid:'P001',
	name: {first:'Peter', last:'Chan'},
	hp: 80,
	characters:[ 
		{charID:'ch001', 
		charName:'CH001Name', 
		charLv:3, 
		charWeapons:[
			{weaponID:'wp001', weaponName:'WP001Name', weaponLv:10}, 
			{weaponID:'wp002', weaponName:'WP002Name', weaponLv:13}]
		},     
		{charID:'ch002', 
		charName:'CH002Name', 
		charLv:2, 
		charWeapons:[
			{weaponID:'wp003', weaponName:'WP003Name', weaponLv:7}, 
			{weaponID:'wp004', weaponName:'WP004Name', weaponLv:18}]
		}	
	]}
	,
	{playerid:'P002',
	name: {first:'Paul', last:'Leung'},
	hp: 60,
	characters:[ 
		{charID:'ch001', 
		charName:'CH001Name', 
		charLv:2, 
		charWeapons:[
			{weaponID:'wp001', weaponName:'WP001Name', weaponLv:11}, 
			{weaponID:'wp003', weaponName:'WP003Name', weaponLv:6}]
		},     
		{charID:'ch003', 
		charName:'CH003Name', 
		charLv:4, 
		charWeapons:[
			{weaponID:'wp002', weaponName:'WP002Name', weaponLv:5}, 
			{weaponID:'wp004', weaponName:'WP004Name', weaponLv:12}]
		}	
	]}
	,
	{playerid:'P003',
	name: {first:'Mary', last:'Wong'},
	hp: 70,
	characters:[ 
		{charID:'ch002', 
		charName:'CH002Name', 
		charLv:4, 
		charWeapons:[
			{weaponID:'wp002', weaponName:'WP002Name', weaponLv:9}, 
			{weaponID:'wp003', weaponName:'WP003Name', weaponLv:13}]
		},     
		{charID:'ch003', 
		charName:'CH003Name', 
		charLv:4, 
		charWeapons:[
			{weaponID:'wp001', weaponName:'WP001Name', weaponLv:3}, 
			{weaponID:'wp004', weaponName:'WP004Name', weaponLv:14}]
		}	
	]}
])

db.players.insert(
	{playerid:'P004',
	name: {first:'John', last:'Tong'},
	hp: 75,
	characters:[ 
		{charID:'ch001', 
		charName:'CH001Name', 
		charLv:5, 
		charWeapons:[
			{weaponID:'wp002', weaponName:'WP002Name', weaponLv:9}]
		},     
		{charID:'ch004', 
		charName:'CH004Name', 
		charLv:6, 
		charWeapons:[
			{weaponID:'wp001', weaponName:'WP001Name', weaponLv:5}, 
			{weaponID:'wp003', weaponName:'WP003Name', weaponLv:7},
			{weaponID:'wp004', weaponName:'WP004Name', weaponLv:18}]
		}	
	]}
);