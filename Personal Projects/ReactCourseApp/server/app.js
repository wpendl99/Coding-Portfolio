const express = require("express");
const Joi = require("joi");
const app = express();

app.use(express.json());

// Simple in-app Demo Database
const courses = [
	{ id: 1, name: "Course1" },
	{ id: 2, name: "Course2" },
	{ id: 3, name: "Course3" },
];

// Get All Courses
app.get("/api/courses", (req, res) => {
	res.send(courses);
});

// Get Specified Course
app.get("/api/courses/:id", (req, res) => {
	const course = courses.find((c) => c.id === parseInt(req.params.id));
	if (!course)
		return res.status(404).send("The course with the given ID was not found");
	res.send(course.name);
});

// Working with option parameters
app.get("/api/courses/:year/:month", (req, res) => {
	res.send(req.params); // This is how you get the option paraments that start with a ? in the address
});

// Add Course
app.post("/api/courses", (req, res) => {
	// Validate the Request Body
	const { error } = validateCourse(req.body);
	if (error) return res.status(404).send(result.error.details[0].message);

	const course = {
		id: courses.length + 1,
		name: req.body.name,
	};
	courses.push(course);
	res.send(course);
});

// Update Course name
app.put("/api/courses/:id", (req, res) => {
	// Find the Course
	const course = courses.find((c) => c.id === parseInt(req.params.id));
	if (!course)
		return res.status(404).send("The course with the given ID was not found");

	// Validate the course
	const { error } = validateCourse(req.body);
	if (error) {
		res.status(404).send(result.error.details[0].message);
		return;
	}

	// Make the changes
	course.name = req.body.name;
	res.send(course);
});

// Delete specified course
app.delete("/api/courses/:id", (req, res) => {
	// Look up course
	const course = courses.find((c) => c.id === parseInt(req.params.id));
	if (!course)
		return res.status(404).send("The course with the given ID was not found");

	// Delete
	const index = courses.indexOf(course);
	courses.splice(index, 1);

	res.send(course);
});

// JOI Course Validation
function validateUser(course) {
	const schema = Joi.object({
		course: Joi.string().required(), 
	});

	return schema.validate(course);
}

// PORT
const port = process.env.port || 3001;
app.listen(port, () => console.log(`Listening on port ${port}...`));
