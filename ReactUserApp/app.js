const express = require("express");
require("dotenv").config();
const db = require("./db");
const app = express();

app.use(express.json());

// Get All Users from database
app.get("/", async (req, res) => {
	try {
		let results = await db.all();
		res.json(results);
	} catch (e) {
		console.log(e.message);
		res.status(404).send("There was an error");
	}
});

// Get Specified User from database
app.get("/:id", async (req, res) => {
	try {
		let results = await db.one(req.params.id);
		res.json(results);
	} catch (e) {
		console.log(e.message);
		res.status(404).send("There was an error");
	}
});

// PORT
const port = process.env.port || 3001;
app.listen(port, () => console.log(`Listening on port ${port}...`));
