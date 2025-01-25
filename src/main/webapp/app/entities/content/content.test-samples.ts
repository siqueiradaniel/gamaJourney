import { IContent, NewContent } from './content.model';

export const sampleWithRequiredData: IContent = {
  id: 'c999c644-ead5-4c42-9c60-95079f68bf93',
};

export const sampleWithPartialData: IContent = {
  id: '02329c56-90e7-4ef0-8a40-7f4943ae406b',
};

export const sampleWithFullData: IContent = {
  id: '5a195dc3-23fa-4e94-a85f-cdfab6ee7db6',
  name: 'understated finally whereas',
  description: 'once jealously sheepishly',
};

export const sampleWithNewData: NewContent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
