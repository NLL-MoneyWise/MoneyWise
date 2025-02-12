import '../src/app/globals.css';

const preview = {
    decorators: [(Story) => <Story />],
    parameters: {
        controls: {
            matchers: {
                color: /(background|color)$/i,
                date: /Date$/i
            }
        }
    }
};

export default preview;
