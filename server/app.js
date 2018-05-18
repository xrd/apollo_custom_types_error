const express = require('express');
const { graphqlExpress, graphiqlExpress } = require( 'apollo-server-express' );
const bodyParser = require('body-parser');
const { makeExecutableSchema } = require('graphql-tools');

// Some fake data
const links = [
    "https://example.com",
    "https://myspace.com"
];

// The GraphQL schema in string form
const typeDefs = `
  type Query { links: [URL] }
  scalar URL
`;

// The resolvers
const resolvers = {
  Query: { links: () => links },
};

// Put together a schema
const schema = makeExecutableSchema({
  typeDefs,
  resolvers,
});

const PORT = 3000;

const app = express();

// bodyParser is needed just for POST.
app.use('/graphql', bodyParser.json(), graphqlExpress({ schema }));
app.get('/graphiql', graphiqlExpress({ endpointURL: '/graphql' })); // if you want GraphiQL enabled

app.listen(PORT);
