const mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/gamedb2', {useNewUrlParser: true, useUnifiedTopology: true, useCreateIndex: true, useFindAndModify: false});

const db = mongoose.connection;

// To use the model in Q1
const Weapon = require('./models/weapon.js');  

// error
db.on('error', ()=> {
    console.log('Connection Error');
});    

// API: https://mongoosejs.com/docs/api/model.html


// Q2 - Write your code here



// once the connection open
db.once('open', async ()=> {
    console.log('MongoDB connected');

    db.dropDatabase();      // *** FOR DEMO ONLY ***

    await insertWeapon( {weaponId:'W001', weaponName:'WN1', price:40, stock:171 } ); 
    await insertWeapon( {weaponId:'W002', weaponName:'WN2', price:30, stock:262 } ); 
    await insertWeapon( {weaponId:'W003', weaponName:'WN3', price:20, stock:143 } ); 
    await insertWeapon( {weaponId:'W004', weaponName:'WN4', price:10, stock:254 } ); 

    await printAllWeapons();

    let w004 = await getWeaponById('W004');
    console.log(w004);

    w004.stock -= 10;
    let oldWeapon = await updateWeapon(w004);
    console.log(oldWeapon);

    await printAllWeapons();
    console.log();

    await updateWeaponStock('W004',300);
    await printAllWeapons();
    console.log();


    let deletedWeapon = await deleteWeapon(w004);
    await printAllWeapons();

});