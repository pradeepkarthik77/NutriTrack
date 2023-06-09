const express = require('express')
const app = express()
const MongoClient = require('mongodb').MongoClient
const bcrypt = require("bcrypt")
const { query } = require('express')
const multer = require('multer');

const url = "<your_mongodb_url>"   

const saltRounds = 10

const upload = multer({ dest: 'uploads/' ,fieldname: 'file'});

app.use(express.json())

MongoClient.connect(url,(err,db) => {
    if(err)
    {
        console.log(err);
        return;
    }

    console.log("Connected to Mongodb")

    var myDb = db.db("login");
 
    const collection = myDb.collection('myTable')

    const datacoll = myDb.collection("UserData")

    const bugscollection = myDb.collection("BugData")

    const watercollection = myDb.collection("WaterCollection")

        app.post('/signup', async (req, res) => 
        {

            hashpwd = await bcrypt.hash(req.body.password,saltRounds)

            const newUser = {
                name: req.body.name,
                email: req.body.email,
                password: hashpwd
            }

            const query = { email: newUser.email }

            console.log(newUser)

            collection.findOne(query, (err, result) => {

                if (result == null) 
                {   
                    collection.insertOne(newUser, (err, result) => {
                        res.status(200).send()
                    })
                } else {
                    res.status(400).send()
                }

            })

            console.log("received request")

        })

        app.post('/login', async (req, res) => {

            const query = {
                email: req.body.email
            }

            console.log(query)

            collection.findOne(query, async (err, result) => {

                console.log(result)

                if (result != null) 
                {

                    const cmp = await bcrypt.compare(req.body.password,result.password)

                    if(cmp)
                    {

                    const objToSend = 
                    {
                        name: result.name,
                        email: result.email,
                        age: result.age,
                        gender: result.gender,
                        goal_calorie: result.goal_calorie,
                        activity_level: result.activity_level,
                        height: result.height,
                        weight: result.weight
                    }

                    res.status(200).send(JSON.stringify(objToSend))
                    }
                    else{
                        res.status(404).send()
                    }

                } else {
                    res.status(400).send()
                }

            })

            console.log("received login")
        })

        app.post('/senddata', async (req, res) => 
        {
            console.log("received request for userdata saving")

            size = parseInt(req.body.size) 

            let reqbody = JSON.stringify(req.body)

            let reqobj = JSON.parse(reqbody)
        

            for(let i=1;i<=size;i++)
            {
                let x = i+""

                string = reqobj[x]

                let arr = string.split(",")

                const newdata = {
                    item_id: arr[0],
                    item_name: arr[1],
                    serving_size: arr[2],
                    calories: arr[3],
                    fat: arr[4],
                    saturated_fat: arr[5],
                    trans_fat: arr[6],
                    cholesterol: arr[7],
                    sodium: arr[8],
                    carbohydrates: arr[9],
                    dietary_fiber: arr[10],
                    sugar: arr[11],
                    added_sugar: arr[12],
                    protein: arr[13],
                    vitamin_D: arr[14],
                    calcium: arr[15],
                    iron: arr[16],
                    potassium: arr[17],
                    vitamin_A: arr[18],
                    vitamin_C: arr[19],
                    manganese: arr[20],
                    vitamin_K: arr[21],
                    item_unit: arr[22],
                    item_image_type: arr[23],
                    item_type: arr[24],
                    Date: arr[25],
                    email: req.body.email
                }

                datacoll.insertOne(newdata, (err, result) => {
                })
            }

            res.status(200).send()

            

            // const newUser = {
            //     name: req.body.name,
            //     email: req.body.email,
            //     name: req.body.name,
            //     password: hashpwd
            // }

            // const query = { email: newUser.email }

            // collection.findOne(query, (err, result) => {

            //     if (result == null) 
            //     {   
            //         collection.insertOne(newUser, (err, result) => {
            //             res.status(200).send()
            //         })
            //     } else {
            //         res.status(400).send()
            //     }

            // })

        })

        app.post("/send_signup_details", async (req,res) => {

            console.log("received request to send signup data")

            let query = {
                email: req.body.email 
                }

                collection.findOne(query, async (err, result) => {

                    if (result != null) 
                    {
                        
                        let toupdate = {$set : {age: req.body.age, gender: req.body.gender, height: req.body.height, weight: req.body.weight, activity_level: req.body.activity_level, goal_calorie : req.body.goal_calorie }}

                        collection.updateOne(query,toupdate)

                        res.status(200).send()
                    } 
                    else {
                        res.status(400).send()
                    }
    
                })

        })

        app.post("/send_edit_profile", async (req,res) => {

            console.log("received request to edit profile data")

            let query = {
                email: req.body.email
                }

                collection.findOne(query, async (err, result) => {

                    if (result != null) 
                    {
                        
                        let toupdate = {$set : {age: req.body.age, gender: req.body.gender, height: req.body.height, weight: req.body.weight, activity_level: req.body.activity_level, goal_calorie : req.body.goal_calorie,name: req.body.name }}

                        collection.updateOne(query,toupdate)

                        res.status(200).send()
                    } 
                    else {
                        res.status(400).send()
                    }
    
                })

        })

        app.post("/send_bug_report",async (req,res) => {

            console.log("Got a report log")

            let record = {
                email: req.body.email,
                description: req.body.description
            }

            bugscollection.insertOne(record,(err,result)=> {
                if(err)
                {
                    res.status(400).send()
                    return
                }
                else 
                {
                    res.status(200).send()
                }
            })

        })

        app.post("/send_water_log",async (req,res) => {
            console.log(req.body.email)
            console.log(req.body.dates)

            let dateslist = JSON.parse(req.body.dates)
            let email_variable = req.body.email

            for(let date_string in dateslist)
            {
                let query = {
                    email: email_variable,
                    date: date_string
                }

                update = {$set: {water_intake: dateslist[date_string]}}

                options = {upsert: true}

                watercollection.updateOne(query,update,options,async (err,result) => {

                    if(err)
                    {
                        console.log(err)
                        res.status(400).send()
                        return
                    }
                })
            }

            res.status(200).send()
            
        })

        app.post("/send_goal_activity", async (req,res) => {

            console.log("received request to goal activity")

            let query = {
                email: req.body.email
                }

                collection.findOne(query, async (err, result) => {

                    if (result != null) 
                    {
                        
                        value = req.body.type

                        update = {}

                        options = {upsert: true}

                        switch(value)
                        {
                            case "Goal_Calorie": update = {$set: {goal_calorie: req.body.value}};break;
                            case "Goal_Water" : update = {$set: {goal_water: req.body.value}};break;
                            case "Goal_Protein" : update = {$set: {goal_protein: req.body.value}};break;
                            case "Goal_Carbs" : update = {$set: {goal_carbs: req.body.value}};break;
                            case "Goal_Fat" : update = {$set: {goal_fat: req.body.value}};break;
                            case "Goal_Fiber" : update = {$set: {goal_fiber: req.body.value}};break;
                            case "Goal_Vitamin_C" : update = {$set: {goal_vitamin_c: req.body.value}};break;
                        }

                        collection.updateOne(query,update,options,async (err,result) => {
                            if(err)
                            {
                                res.status(400).send()
                                return 
                            }
                            else 
                            {
                                res.status(200).send()
                                return
                            }
                        })
                    } 
                    else {
                        res.status(400).send()
                    }
    
                })

        })

        app.post('/upload_img', upload.single('file'), (req, res) => {

            console.log("Got upload imagefi")

            // Handle the uploaded file
            if (req.file) 
            {
              // File received and saved on the server
              res.status(200).send('File uploaded successfully.');
            } else {
              // No file received
              res.status(400).send('No file uploaded.');
            }
          });

})


app.listen(3000, () => {
    console.log("Listening on port 3000...")
})
