const mariadb = require("mariadb");

const pool = mariadb.createPool({
	connectionLimit: 5, // Limit can be a lot bigger than this
	user: process.env.MDB_TEST_USER,
	password: process.env.MDB_TEST_PASS,
	host: "127.0.0.1", // Local Host
	database: "TestDatabase", 
	port: 3306,
	connectTimeout: 2000,
	acquireTimeout: 2000,
});

let testdb = {};

// Query All Users from db
testdb.all = () => {
	return new Promise(async (resolve, reject) => {
		try {
      // Grab Connection from Pool
			let conn = await pool.getConnection();
      
      // Make Query
			await conn.query("SELECT 1 as val");
			let res = await conn.query(`SELECT * FROM TestDatabase.users`);
      
      // Close Connection and return it to pool
      if (conn) conn.end();
			resolve(res);
		} catch (e) {
			console.log(e);
			reject(e);
		}
	});
};

// Query Specified User from db
testdb.one = (id) => {
	return new Promise(async (resolve, reject) => {
		try {
      // Grab Connection from Pool
			let conn = await pool.getConnection();
      
      // Make Query
			await conn.query("SELECT 1 as val");
			let result = await conn.query(
				`SELECT * FROM TestDatabase.users WHERE userid=?`,
				[id]
			);
      
      // Close Connection and return it to pool
      if (conn) conn.end();
			resolve(result[0]);
		} catch (e) {
			console.log(e);
			reject(e);
		}
	});
};

module.exports = testdb;
