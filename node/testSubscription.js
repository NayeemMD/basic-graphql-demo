const { execute, subscribe } = require('graphql');
const { SubscriptionClient } = require('subscriptions-transport-ws');
const WebSocket = require('ws');
const gql = require('graphql-tag');

const BOOK_ADDED_SUBSCRIPTION = gql`
    subscription {
        bookAdded {
            uuid
            title
            author
        }
    }
`;

const client = new SubscriptionClient('ws://localhost:8081/subscriptions', { reconnect: true }, WebSocket);

client.request({ query: BOOK_ADDED_SUBSCRIPTION }).subscribe({
    next(data) {
        console.log('Subscription data:', data);
    },
    error(err) {
        console.error('Subscription error:', err);
    },
});